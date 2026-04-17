package com.tusuapp.coreapi.controllers.payments;

import com.tusuapp.coreapi.models.CreditTransaction;
import com.tusuapp.coreapi.repositories.CreditTransactionRepo;
import com.tusuapp.coreapi.services.user.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

@RestController
@RequestMapping("/user/credits")
@RequiredArgsConstructor
public class CreditsController {

    private final CreditService creditService;
    private final CreditTransactionRepo creditTransactionRepo;

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {
        Double balance = creditService.getCurrentUserBalance();
        return ResponseEntity.ok(Map.of("balance", balance));
    }

    // TEMPORARY DEBUG — remove after confirming transactions work
    @PostMapping("/debug-transaction")
    public ResponseEntity<?> debugTransaction() {
        try {
            CreditTransaction tx = new CreditTransaction();
            tx.setUserId(getCurrentUserId());
            tx.setAmount(1.0);
            tx.setType("CREDIT");
            tx.setDescription("debug test");
            tx.setCreatedAt(LocalDateTime.now());
            CreditTransaction saved = creditTransactionRepo.save(tx);
            return ResponseEntity.ok(Map.of("saved", true, "id", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
