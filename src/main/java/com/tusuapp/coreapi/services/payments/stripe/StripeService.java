package com.tusuapp.coreapi.services.payments.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tusuapp.coreapi.constants.TransactionConstants;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.CreditPoint;
import com.tusuapp.coreapi.models.PaymentSession;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.PaymentSessionRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.tusuapp.coreapi.constants.TransactionConstants.TRANSACTION_TYPE_BALANCE_BUY;
import static com.tusuapp.coreapi.constants.TransactionConstants.TRANSACTION_TYPE_CREDIT_BUY;
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


//    @Value("${STRIPE_API_KEY}")
//    String STRIPE_API_KEY;

    @Autowired
    StripeService() {
        Stripe.apiKey = "sk_test_51MNzxoSENC2ebtoEDwsTy6jaF9atPHrYv8bElPv0fL4fWkEEbzZU85EiXvMZ4pjr0bW1AXUiwtzHASaqPoLBOhPb00WF7BRNNx";
    }

    public ResponseEntity<?> initiateBookingPayment(Long requestId) throws StripeException {
        BookingRequest bookingRequest = bookingRequestRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("BookingRequest not found"));

        BigDecimal totalAmount = bookingRequest.getHourlyCharge().add(bookingRequest.getCommissionAmount());

        Map<String, String> metadata = new HashMap<>();
        metadata.put("app_id", bookingRequest.getId().toString());

        PaymentSession paymentSession = createStripePaymentSession(
                "Buying credit points",
                totalAmount,
                metadata
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
                metadata
        );

        JSONObject response = new JSONObject();
        response.put("session", paymentSession);
        response.put("payment_url", Session.retrieve(paymentSession.getStripeSessionId()).getUrl());

        return ResponseEntity.ok(response.toMap());
    }

    private PaymentSession createStripePaymentSession(String productName,
                                                      BigDecimal amount,
                                                      Map<String, String> metadata) throws StripeException {
        PaymentSession paymentSession = new PaymentSession();
        paymentSession.setSessionId(UUID.randomUUID().toString());
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
                                        .setUnitAmountDecimal(amount)
                                        .setProductData(productBuilder.build())
                                        .build()
                        ).build()
        );

        // Create session with Stripe
        Session session = Session.create(paramsBuilder.build());
        paymentSession.setStripeSessionId(session.getId());
        paymentSession.setTransactionType(TRANSACTION_TYPE_BALANCE_BUY);
        paymentSessionRepo.save(paymentSession);

        return paymentSession;
    }


    public ResponseEntity<?> completePayment(String sessionID) {
        try {
            Optional<PaymentSession> sessionOptional = paymentSessionRepo.findById(sessionID);
            if(sessionOptional.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
            }
            PaymentSession paymentSession = sessionOptional.get();
            if(paymentSession.getIsCompleted()){
                return ResponseEntity.ok("Payment already completed");
            }
            Session session = Session.retrieve(paymentSession.getStripeSessionId());
            if(!"completed".equals(session.getStatus())){
                //Add additional tracking items to keep this tracked
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't verify with stripe for payment completion");
            }
            if(paymentSession.getTransactionType().equals(TRANSACTION_TYPE_CREDIT_BUY)){
                //credit to user's account

            }
            if(paymentSession.getTransactionType().equals(TRANSACTION_TYPE_BALANCE_BUY)){
                //buy that on going class
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }


    }
}
