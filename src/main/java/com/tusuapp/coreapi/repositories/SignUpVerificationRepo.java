package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.SignUpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpVerificationRepo extends JpaRepository<SignUpVerification, String> {
}
