package com.tusuapp.coreapi.services.user;

import com.tusuapp.coreapi.models.CreditTransaction;
import com.tusuapp.coreapi.models.UserWallet;
import com.tusuapp.coreapi.models.dtos.payments.CreditTransactionDto;
import com.tusuapp.coreapi.repositories.CreditPointRepo;
import com.tusuapp.coreapi.repositories.CreditTransactionRepo;
import com.tusuapp.coreapi.services.user.notifications.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

@Service
public class CreditService {

    @Autowired
    private CreditPointRepo creditPointRepo;

    @Autowired
    private CreditTransactionRepo creditTransactionRepo;

    @Autowired
    private NotificationService notificationService;

    // ── Balance ──────────────────────────────────────────────────────────────

    public boolean currentUserHasEnoughCredit(Double amount) {
        try {
            Optional<UserWallet> creditPointOptional = creditPointRepo.findByUserId(getCurrentUserId());
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(getCurrentUserId(), 0.0));
                return false;
            }
            return creditPointOptional.get().getBalance() >= amount;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Double getCurrentUserBalance() {
        try {
            Optional<UserWallet> creditPointOptional = creditPointRepo.findByUserId(getCurrentUserId());
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(getCurrentUserId(), 0.0));
                return 0.0;
            }
            return creditPointOptional.get().getBalance();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // ── Transactions ─────────────────────────────────────────────────────────

    public ResponseEntity<?> getTransactions(int page, int size) {
        Page<CreditTransaction> txPage = creditTransactionRepo
                .findByUserIdOrderByCreatedAtDesc(getCurrentUserId(), PageRequest.of(page, size));
        Page<CreditTransactionDto> dtoPage = txPage.map(CreditTransactionDto::from);
        return ResponseEntity.ok(Map.of(
                "transactions", dtoPage.getContent(),
                "totalElements", dtoPage.getTotalElements(),
                "totalPages", dtoPage.getTotalPages(),
                "page", dtoPage.getNumber(),
                "size", dtoPage.getSize()
        ));
    }

    // ── Credit / Debit ───────────────────────────────────────────────────────

    public void addCredits(Long userId, Double amount) {
        addCredits(userId, amount, "Credits purchased");
    }

    public void addCredits(Long userId, Double amount, String description) {
        try {
            Optional<UserWallet> creditPointOptional = creditPointRepo.findByUserId(userId);
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(userId, amount));
            } else {
                UserWallet creditPoint = creditPointOptional.get();
                creditPoint.setBalance(creditPoint.getBalance() + amount);
                try {
                    creditPoint.setUpdatedBy(getCurrentUserId());
                } catch (Exception e) {
                    creditPoint.setUpdatedBy(-3L);
                }
                creditPoint.setUpdatedAt(LocalDateTime.now());
                creditPointRepo.save(creditPoint);
            }
            notificationService.addNotification(userId,
                    amount + " credits have been added to your account", description, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logTransaction(userId, amount, "CREDIT", description);
    }

    public void addAdminCredits(Long studentId, Double amount) {
        try {
            Optional<UserWallet> creditPointOptional = creditPointRepo.findByUserId(studentId);
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(studentId, amount));
            } else {
                UserWallet creditPoint = creditPointOptional.get();
                creditPoint.setBalance(creditPoint.getBalance() + amount);
                creditPoint.setUpdatedBy(getCurrentUserId());
                creditPoint.setUpdatedAt(LocalDateTime.now());
                creditPointRepo.save(creditPoint);
            }
            notificationService.addNotification(studentId, "Credits Added", "Credited by Admin", true);
            logTransaction(studentId, amount, "CREDIT", "Credits added by admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean reduceCredits(Long studentId, Double amount) {
        try {
            Optional<UserWallet> creditPointOptional = creditPointRepo.findByUserId(studentId);
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(studentId, 0.0));
                throw new IllegalArgumentException("No credit to reduce");
            }
            UserWallet creditPoint = creditPointOptional.get();
            if (creditPoint.getBalance() < amount) {
                throw new IllegalArgumentException("Not enough balance to reduce");
            }
            creditPoint.setUpdatedBy(getCurrentUserId());
            creditPoint.setBalance(creditPoint.getBalance() - amount);
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        logTransaction(studentId, amount, "DEBIT", "Payment for class booking");
        return true;
    }

    // ── Payout helpers (called by PayoutService / AdminPayoutService) ─────────

    public void logWithdrawal(Long userId, Double amount) {
        logTransaction(userId, amount, "DEBIT", "Withdrawal requested");
    }

    public void logPayoutRejected(Long userId, Double amount) {
        logTransaction(userId, amount, "CREDIT", "Withdrawal rejected - amount refunded");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void logTransaction(Long userId, Double amount, String type, String description) {
        try {
            System.out.println("[CreditService] logTransaction called: userId=" + userId + " amount=" + amount + " type=" + type);
            CreditTransaction tx = CreditTransaction.builder()
                    .userId(userId)
                    .amount(amount)
                    .type(type)
                    .description(description)
                    .build();
            CreditTransaction saved = creditTransactionRepo.save(tx);
            System.out.println("[CreditService] logTransaction saved with id=" + saved.getId());
        } catch (Exception e) {
            System.err.println("[CreditService] logTransaction FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private UserWallet getNewCreditPoint(Long studentId, Double amount) {
        UserWallet creditPoint = new UserWallet();
        creditPoint.setUserId(studentId);
        creditPoint.setBalance(amount);
        creditPoint.setCreatedAt(LocalDateTime.now());
        creditPoint.setUpdatedBy(getCurrentUserId());
        return creditPoint;
    }
}
