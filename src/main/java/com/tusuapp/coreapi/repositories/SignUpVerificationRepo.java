package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.SignUpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SignUpVerificationRepo extends JpaRepository<SignUpVerification, String> {

    List<SignUpVerification> findAllByCreatedAtBeforeAndIsEmailVerifiedFalseOrIsPhoneVerifiedFalse(LocalDateTime currentUTCTime);
}
