package com.tusuapp.coreapi.controllers.admin.tutors;

import com.tusuapp.coreapi.models.dtos.payments.PayoutProcessDto;
import com.tusuapp.coreapi.models.dtos.payments.PayoutResponseDto;
import com.tusuapp.coreapi.services.admin.tutors.AdminPayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/admin/tutor/payouts")
@RequiredArgsConstructor
public class AdminTutorPayoutController {

    private final AdminPayoutService adminPayoutService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PayoutResponseDto>> getPayoutRequests() {
        return ResponseEntity.ok(adminPayoutService.getAllPayoutRequests());
    }

    @PostMapping("/process")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> processPayout(@RequestBody PayoutProcessDto processDto) {
        if (processDto.getRequestId() == null || processDto.getStatus() == null) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Request ID and Status are required");
        }

        return ResponseEntity.ok(adminPayoutService.processPayout(processDto));
    }
}
