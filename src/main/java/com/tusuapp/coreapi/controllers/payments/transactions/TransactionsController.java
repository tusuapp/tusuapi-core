package com.tusuapp.coreapi.controllers.payments.transactions;

import com.tusuapp.coreapi.services.user.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/payments/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final CreditService creditService;

    @GetMapping
    public ResponseEntity<?> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return creditService.getTransactions(page, size);
    }
}
