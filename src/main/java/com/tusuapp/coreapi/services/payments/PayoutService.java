package com.tusuapp.coreapi.services.payments;

import com.tusuapp.coreapi.models.PayoutRequest;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.UserWallet;
import com.tusuapp.coreapi.models.enums.PayoutStatus;
import com.tusuapp.coreapi.repositories.PayoutRequestRepo;
import com.tusuapp.coreapi.repositories.UserWalletRepo;
import com.tusuapp.coreapi.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayoutService {

    private final PayoutRequestRepo payoutRequestRepo;
    private final UserWalletRepo userWalletRepo;

    @Transactional
    public PayoutRequest createPayoutRequest(Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Long currentUserId = SessionUtil.getCurrentUserId();

        UserWallet wallet = userWalletRepo.findByUserId(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user"));

        if (wallet.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Logic choice: Do we deduct balance now or when approved?
        // Usually, we should deduct or hold. For simplicity and safety against double
        // requests,
        // we will deduct it now. If rejected, we must refund.
        // However, checking requirements, user just said "requesting a new payout".
        // I'll stick to simple check for now, but to be safe, I should probably deduct
        // it to prevent multiple requests draining balance.
        // Let's deduct it now.
        wallet.setBalance(wallet.getBalance() - amount);
        userWalletRepo.save(wallet);

        // We need a User entity reference for the ManyToOne relationship
        User tutorReference = new User();
        tutorReference.setId(currentUserId);

        PayoutRequest request = new PayoutRequest();
        request.setTutor(tutorReference);
        request.setAmount(amount);
        request.setStatus(PayoutStatus.PENDING);
        request.setCurrency("USD");

        return payoutRequestRepo.save(request);
    }

    public List<PayoutRequest> getPayoutRequests() {
        Long currentUserId = SessionUtil.getCurrentUserId();
        return payoutRequestRepo.findAllByTutorIdOrderByCreatedAtDesc(currentUserId);
    }
}
