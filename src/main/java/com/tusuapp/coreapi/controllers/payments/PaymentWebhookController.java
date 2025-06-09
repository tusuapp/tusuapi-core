//package com.tusuapp.coreapi.controllers.payments;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/payments")
//public class PaymentWebhookController {
//
//
//    @PostMapping("/webhook")
//    public ResponseEntity<String> handlePaymentWebhook(@RequestBody String payload,
//                                                       @RequestHeader("X-Signature") String signature) {
//        // 1. Verify webhook signature (important for security!)
//        boolean isValid = verifySignature(payload, signature);
//        if (!isValid) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
//        }
//
//        // 2. Parse payload JSON to get payment details
//        PaymentEvent event = parsePayload(payload);
//
//        // 3. Handle payment success event
//        if ("payment_success".equals(event.getType())) {
//            // Update your booking/order status to paid
//            bookingService.markAsPaid(event.getTransactionId());
//        }
//
//        // 4. Return 200 OK to acknowledge webhook
//        return ResponseEntity.ok("Received");
//    }
//
//    private boolean verifySignature(String payload, String signature) {
//        // Implement provider-specific signature verification logic
//        // Usually involves HMAC with a secret key
//        return true; // Placeholder
//    }
//
//    private PaymentEvent parsePayload(String payload) {
//        // Map JSON payload to your PaymentEvent DTO
//        // Use Jackson ObjectMapper or similar
//        return new PaymentEvent(); // Placeholder
//    }
//}
