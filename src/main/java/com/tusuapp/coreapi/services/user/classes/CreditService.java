package com.tusuapp.coreapi.services.user.classes;

import com.tusuapp.coreapi.models.CredPointMaster;

import com.tusuapp.coreapi.repositories.CreditPointRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;


@Service
public class CreditService {

    @Autowired
    private CreditPointRepo creditPointRepo;


    public boolean addCredits(Long studentId, Double amount){
        try{
            Optional<CredPointMaster> creditPointOptional = creditPointRepo.findByStudentId(studentId);
            if(creditPointOptional.isEmpty()){
                creditPointRepo.save(getNewCreditPoint(studentId,amount));
            }
            CredPointMaster creditPoint = creditPointOptional.get();
            creditPoint.setBalance(creditPoint.getBalance() + amount);
            creditPoint.setUpdatedBy(Math.toIntExact(getCurrentUserId()));
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean reduceCredits(Long studentId, Double amount) {
        try{
            Optional<CredPointMaster> creditPointOptional = creditPointRepo.findByStudentId(studentId);
            if(creditPointOptional.isEmpty()){
                creditPointRepo.save(getNewCreditPoint(studentId,0.0));
                throw new IllegalArgumentException("No credit to reduce");
            }
            CredPointMaster creditPoint = creditPointOptional.get();
            if(creditPoint.getBalance() < amount){
                throw new IllegalArgumentException("No enough balance to reduce");
            }
            creditPoint.setUpdatedBy(Math.toIntExact(getCurrentUserId()));
            creditPoint.setBalance(creditPoint.getBalance() - amount);
            System.out.println("Reduced credits = " + creditPoint.getBalance());
            creditPoint.setUpdatedAt(LocalDateTime.now());
            creditPointRepo.save(creditPoint);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private CredPointMaster getNewCreditPoint(Long studentId, Double amount) {
        CredPointMaster creditPoint = new CredPointMaster();
        creditPoint.setStudentId(studentId);
        creditPoint.setBalance(amount);
        creditPoint.setCreatedAt(LocalDateTime.now());
        return creditPoint;
    }
}
