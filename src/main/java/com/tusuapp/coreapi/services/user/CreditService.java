package com.tusuapp.coreapi.services.user;

import com.tusuapp.coreapi.models.UserWallet;
import com.tusuapp.coreapi.repositories.CreditPointRepo;
import com.tusuapp.coreapi.services.user.notifications.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;


@Service
public class CreditService {

    @Autowired
    private CreditPointRepo creditPointRepo;

    @Autowired
    private NotificationService notificationService;

    public boolean currentUserHasEnoughCredit(Double amount) {
        try {
            System.out.println("currentUserHasEnoughCredit user " + getCurrentUserId());
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
            System.out.println("currentUserHasEnoughCredit user " + getCurrentUserId());
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

    public void addCredits(Long studentId, Double amount) {
        try {
            Optional<UserWallet> creditPointOptional = creditPointRepo.findByUserId(studentId);
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(studentId, amount));
                return;
            }
            UserWallet creditPoint = creditPointOptional.get();
            creditPoint.setBalance(creditPoint.getBalance() + amount);
            try {
                creditPoint.setUpdatedBy(getCurrentUserId());
                notificationService.addNotification(studentId,
                        amount + " has been added to your account",
                        "Payment has been success and points have been credited", true);
            } catch (Exception e) {
                creditPoint.setUpdatedBy(-3L);
                notificationService.addNotification(studentId,
                        amount + " has been refunded to your account",
                        "Payment has been refunded and request have been cancelled", true);
            }
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
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
                throw new IllegalArgumentException("No enough balance to reduce");
            }
            creditPoint.setUpdatedBy(getCurrentUserId());
            creditPoint.setBalance(creditPoint.getBalance() - amount);
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
            return true;
        } catch (Exception e) {
            return false;
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
