package com.tusuapp.coreapi.services.payments.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tusuapp.coreapi.constants.BookingConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.PaymentSession;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.PaymentSessionRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.services.user.classes.ClassesService;
import com.tusuapp.coreapi.services.user.classes.CreditService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.tusuapp.coreapi.constants.TransactionConstants.TRANSACTION_TYPE_CREDIT_BUY;
import static com.tusuapp.coreapi.constants.TransactionConstants.TRANSACTION_TYPE_REMAINING_BUY;
import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;


@Component
public class StripeService {

    @Autowired
    private PaymentSessionRepo paymentSessionRepo;


    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired
    private BookingRequestRepo bookingRequestRepo;


    String clientBaseURL = "https://tusuapp.com/student/payment";


    @Value("${STRIPE_API_KEY}")
    String STRIPE_API_KEY;

    @Autowired
    private CreditService creditService;

    public ResponseEntity<?> purchaseRemainingCredits(Long requestId, double amount) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        BookingRequest bookingRequest = bookingRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("BookingRequest not found"));

        BigDecimal totalAmount = BigDecimal.valueOf(amount);
        System.out.println(totalAmount);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("booking_id", bookingRequest.getId().toString());
        metadata.put("type", "remaining_purchase");
        PaymentSession paymentSession = createStripePaymentSession(
                "Buying credit points",
                totalAmount,
                metadata,
                TRANSACTION_TYPE_REMAINING_BUY
        );

        JSONObject response = new JSONObject();
        response.put("session", paymentSession);
        response.put("payment_url", Session.retrieve(paymentSession.getStripeSessionId()).getUrl());
        return ResponseEntity.ok(response.toMap());
    }

    public ResponseEntity<?> buyCredits(Integer amount) throws StripeException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("type", "wallet_top_up");
        metadata.put("buyer_id", String.valueOf(getCurrentUserId()));

        PaymentSession paymentSession = createStripePaymentSession(
                "Buy credit points",
                BigDecimal.valueOf(amount),
                metadata,
                TRANSACTION_TYPE_CREDIT_BUY
        );

        JSONObject response = new JSONObject();
        response.put("session", paymentSession);
        response.put("payment_url", Session.retrieve(paymentSession.getStripeSessionId()).getUrl());

        return ResponseEntity.ok(response.toMap());
    }

    private PaymentSession createStripePaymentSession(String productName,
                                                      BigDecimal amount,
                                                      Map<String, String> metadata,
                                                      String purchaseType) throws StripeException {
        PaymentSession paymentSession = new PaymentSession();
        paymentSession.setSessionId(UUID.randomUUID().toString());
        paymentSession.setTotalAmount(Double.valueOf(amount.toString()));
        //TODO set tutor id also

        if(purchaseType.equals(TRANSACTION_TYPE_REMAINING_BUY)){
            paymentSession.setBookingRequestId(Long.valueOf(metadata.get("booking_id")));
        }
        paymentSession.setStudentId(getCurrentUserId());
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(clientBaseURL + "/success?session_id=" + paymentSession.getSessionId())
                .setCancelUrl(clientBaseURL + "/failure");

        SessionCreateParams.LineItem.PriceData.ProductData.Builder productBuilder =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productName);

        if (metadata != null) {
            metadata.forEach(productBuilder::putMetadata);
        }

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("USD")
                                        .setUnitAmountDecimal(amount.multiply(BigDecimal.valueOf(100)))
                                        .setProductData(productBuilder.build())
                                        .build()
                        ).build()
        );

        // Create session with Stripe
        Session session = Session.create(paramsBuilder.build());
        paymentSession.setStripeSessionId(session.getId());
        paymentSession.setTransactionType(purchaseType);
        paymentSessionRepo.save(paymentSession);

        return paymentSession;
    }


    public ResponseEntity<?> completePayment(String sessionID) {
        try {
            JSONObject response = new JSONObject();
            Stripe.apiKey = STRIPE_API_KEY;
            Optional<PaymentSession> sessionOptional = paymentSessionRepo.findById(sessionID);
            if (sessionOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
            }
            PaymentSession paymentSession = sessionOptional.get();
            if (paymentSession.isCompleted()) {
                return ResponseEntity.ok("Payment already completed");
            }
            Session session = Session.retrieve(paymentSession.getStripeSessionId());
            System.out.println(session.getStatus());
            String message = "";
            if (!"complete".equals(session.getStatus())) {
                //Add additional tracking items to keep this tracked
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't verify with stripe for payment completion");
            }
            if (paymentSession.getTransactionType().equals(TRANSACTION_TYPE_CREDIT_BUY)) {
                //credit to user's account
                creditService.addCredits(paymentSession.getStudentId(), paymentSession.getTotalAmount());
                markPaymentSessionAsPaid(paymentSession);
                message = "Credits have been added to student's account";
            }
            if (paymentSession.getTransactionType().equals(TRANSACTION_TYPE_REMAINING_BUY)) {
                message = "Class has been booked. Please wait for tutor confirmation";
                //Sessions should be transactional and non-volatile, first credit and then debit only
                creditService.addCredits(paymentSession.getStudentId(), paymentSession.getTotalAmount());
                BookingRequest bookingRequest = bookingRequestRepo.findById(paymentSession.getBookingRequestId())
                        .orElseThrow(() -> new IllegalArgumentException("No booking request found for session"));
                creditService.reduceCredits(bookingRequest.getStudentId(), bookingRequest.getTotalAmount());
                //book the class in the payment session
                bookingRequest.setIsPaid(true);
                bookingRequest.setStatus(BookingConstants.STATUS_REQUESTED);
                bookingRequest = bookingRequestRepo.save(bookingRequest);
                response.put("bookingRequest",bookingRequest);
                markPaymentSessionAsPaid(paymentSession);
            }
            response.put("message", message);
            return ResponseEntity.ok(response.toMap());
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    public void markPaymentSessionAsPaid(PaymentSession paymentSession){
        paymentSession.setCompleted(true);
        paymentSessionRepo.save(paymentSession);
    }

}
