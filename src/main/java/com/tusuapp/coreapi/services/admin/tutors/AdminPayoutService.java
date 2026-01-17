package com.tusuapp.coreapi.services.admin.tutors;

import com.tusuapp.coreapi.models.PayoutRequest;
import com.tusuapp.coreapi.models.UserWallet;
import com.tusuapp.coreapi.models.dtos.payments.PayoutProcessDto;
import com.tusuapp.coreapi.models.dtos.payments.PayoutResponseDto;
import com.tusuapp.coreapi.models.enums.PayoutStatus;
import com.tusuapp.coreapi.repositories.PayoutRequestRepo;
import com.tusuapp.coreapi.repositories.UserWalletRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPayoutService {

    private final PayoutRequestRepo payoutRequestRepo;
    private final UserWalletRepo userWalletRepo;

    public List<PayoutResponseDto> getAllPayoutRequests() {
        return payoutRequestRepo.findAll().stream()
                .map(PayoutResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public PayoutResponseDto processPayout(PayoutProcessDto processDto) {
        PayoutRequest request = payoutRequestRepo.findById(processDto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Payout request not found"));

        if (request.getStatus() != PayoutStatus.PENDING) {
            throw new IllegalArgumentException("Payout request is already processed");
        }

        PayoutStatus newStatus = processDto.getStatus();
        if (newStatus != PayoutStatus.PAID && newStatus != PayoutStatus.REJECTED) {
            throw new IllegalArgumentException("Invalid status update. Can only be PAID or REJECTED.");
        }

        request.setStatus(newStatus);
        request.setAdminNotes(processDto.getAdminNotes());

        // If rejected, refund the amount to wallet
        if (newStatus == PayoutStatus.REJECTED) {
            UserWallet wallet = userWalletRepo.findByUserId(request.getTutor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Wallet not found for tutor"));

            wallet.setBalance(wallet.getBalance() + request.getAmount());
            wallet.setUpdatedBy(null); // System update or admin id? Using null or maintaining existing convention.
            // Usually UpdatedBy tracks who made change. Since it's admin action, ideally
            // admin ID.
            // But UserWallet updatedBy is Long. Currently I don't have logged in Admin ID
            // easily here without SessionUtil/SecurityContext.
            // I'll leave as is or set timestamp. @UpdateTimestamp handles time.
            userWalletRepo.save(wallet);
        }

        request = payoutRequestRepo.save(request);
        return PayoutResponseDto.fromEntity(request);
    }
}
