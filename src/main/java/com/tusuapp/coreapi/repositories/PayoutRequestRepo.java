package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.PayoutRequest;
import com.tusuapp.coreapi.models.enums.PayoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayoutRequestRepo extends JpaRepository<PayoutRequest, Long> {

    List<PayoutRequest> findAllByTutorIdOrderByCreatedAtDesc(Long tutorId);

    List<PayoutRequest> findAllByTutorIdAndStatusOrderByCreatedAtDesc(Long tutorId, PayoutStatus status);
}
