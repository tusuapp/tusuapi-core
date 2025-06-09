package com.tusuapp.coreapi.controllers.payments.stripe;

import com.tusuapp.coreapi.services.payments.stripe.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/payments/stripe")
public class PaymentGatewayController {

    @Autowired
    private StripeService stripeClient;

    @PostMapping("/checkout-booking")
    public ResponseEntity<?> initiateStripePayment(@RequestParam String bookingRequestId) throws Exception {
        return stripeClient.initiateBookingPayment(Long.parseLong(bookingRequestId));
    }

    @PostMapping("/buy-credits")
    public ResponseEntity<?> buyCredits(@RequestParam String topUpAmount) throws Exception {
        return stripeClient.buyCredits(Integer.valueOf(topUpAmount));
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePurchase(@RequestParam String sessionID){
        return stripeClient.completePayment(sessionID);
    }

}
