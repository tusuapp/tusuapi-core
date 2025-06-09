package com.tusuapp.coreapi.controllers.payments.stripe;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.services.payments.stripe.StripeService;
import com.tusuapp.coreapi.utils.CustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

@RestController
@CrossOrigin("*")
@RequestMapping("/payments/stripe")
public class PaymentGatewayController {

    private StripeService stripeClient;

    private UserInfoRepo userInfoRepo;

    private BookingRequestRepo bookingRequestRepo;


    @Autowired
    PaymentGatewayController(StripeService stripeClient, UserInfoRepo userInfoRepo, BookingRequestRepo bookingRequestRepo) {
        this.stripeClient = stripeClient;
        this.userInfoRepo = userInfoRepo;
        this.bookingRequestRepo = bookingRequestRepo;
    }


    @PostMapping("/checkout")
    public String chargeCard(@RequestHeader(value = "token") String token, @RequestHeader(value = "amount") Double amount, @RequestParam String bookingRequestId) throws Exception {

        Stripe.apiKey = "sk_test_51MNzxoSENC2ebtoEDwsTy6jaF9atPHrYv8bElPv0fL4fWkEEbzZU85EiXvMZ4pjr0bW1AXUiwtzHASaqPoLBOhPb00WF7BRNNx";

        String clientBaseURL = "https://tusuapp.com/student/payment";

        // Start by finding an existing customer record from Stripe or creating a new one if needed
        Optional<User> user = userInfoRepo.findById(getCurrentUserId());
//        Customer customer = CustomerUtil.findOrCreateCustomer(user.get().getEmail(), user.get().getFullName());

        // Next, create a checkout session by adding the details of the checkout
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setCustomer(customer.getId())
                        .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(clientBaseURL + "/failure");


        BookingRequest bookingRequest = bookingRequestRepo.findById(Long.valueOf(bookingRequestId)).get();

        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .putMetadata("app_id", bookingRequest.getId().toString())
                                                        .setName("Buying credit points")
                                                        .build()
                                        )
                                        .setCurrency("USD")
                                        .setUnitAmountDecimal(bookingRequest.getHourlyCharge().add(bookingRequest.getCommissionAmount()))
                                        .build())
                        .build());


        Session session = Session.create(paramsBuilder.build());
        return session.getUrl();
    }
}
