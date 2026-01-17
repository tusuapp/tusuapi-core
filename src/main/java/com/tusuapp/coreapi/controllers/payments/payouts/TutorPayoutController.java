package com.tusuapp.coreapi.controllers.payments.payouts;

import com.tusuapp.coreapi.models.PayoutRequest;
import com.tusuapp.coreapi.models.dtos.payments.PayoutCreationDto;
import com.tusuapp.coreapi.models.dtos.payments.PayoutResponseDto;
import com.tusuapp.coreapi.services.payments.PayoutService;
import lombok.RequiredArgsConstructor;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutor/payouts")
@RequiredArgsConstructor
public class TutorPayoutController {

    private final PayoutService payoutService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<?> requestPayout(
            @RequestBody PayoutCreationDto requestBody) {

        Double amount = requestBody.getAmount();
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        PayoutRequest request = payoutService.createPayoutRequest(amount);
        return ResponseEntity.ok(PayoutResponseDto.fromEntity(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<List<PayoutResponseDto>> getPayoutRequests() {
        // Role check removed

        List<PayoutRequest> requests = payoutService.getPayoutRequests();
        List<PayoutResponseDto> dtos = requests.stream()
                .map(PayoutResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
