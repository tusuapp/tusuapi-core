package com.tusuapp.coreapi.services.user;

import com.tusuapp.coreapi.models.CredPointMaster;
import com.tusuapp.coreapi.repositories.CreditPointRepo;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.isStudent;


@Service
public class CreditService {

    @Autowired
    private CreditPointRepo creditPointRepo;

    public boolean currentUserHasEnoughCredit(Double amount) {
        try {
            System.out.println("currentUserHasEnoughCredit user " + getCurrentUserId());
            Optional<CredPointMaster> creditPointOptional = creditPointRepo.findByStudentId(getCurrentUserId());
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(getCurrentUserId(), 0.0));
                return false;
            }
            System.out.println("current user has balance " + creditPointOptional.get().getBalance());
            return creditPointOptional.get().getBalance() >= amount;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Double getCurrentUserBalance() {
        try {
            System.out.println("currentUserHasEnoughCredit user " + getCurrentUserId());
            Optional<CredPointMaster> creditPointOptional = creditPointRepo.findByStudentId(getCurrentUserId());
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

    public boolean addCredits(Long studentId, Double amount) {
        try {
            Optional<CredPointMaster> creditPointOptional = creditPointRepo.findByStudentId(studentId);
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(studentId, amount));
                return true;
            }
            CredPointMaster creditPoint = creditPointOptional.get();
            creditPoint.setBalance(creditPoint.getBalance() + amount);
            creditPoint.setUpdatedBy(getCurrentUserId());
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reduceCredits(Long studentId, Double amount) {
        try {
            Optional<CredPointMaster> creditPointOptional = creditPointRepo.findByStudentId(studentId);
            if (creditPointOptional.isEmpty()) {
                creditPointRepo.save(getNewCreditPoint(studentId, 0.0));
                throw new IllegalArgumentException("No credit to reduce");
            }
            CredPointMaster creditPoint = creditPointOptional.get();
            if (creditPoint.getBalance() < amount) {
                throw new IllegalArgumentException("No enough balance to reduce");
            }
            creditPoint.setUpdatedBy(getCurrentUserId());
            creditPoint.setBalance(creditPoint.getBalance() - amount);
            System.out.println("Reduced credits = " + creditPoint.getBalance());
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private CredPointMaster getNewCreditPoint(Long studentId, Double amount) {
        CredPointMaster creditPoint = new CredPointMaster();
        creditPoint.setStudentId(studentId);
        creditPoint.setBalance(amount);
        creditPoint.setCreatedAt(LocalDateTime.now());
        creditPoint.setUpdatedBy(getCurrentUserId());
        return creditPoint;
    }
}
