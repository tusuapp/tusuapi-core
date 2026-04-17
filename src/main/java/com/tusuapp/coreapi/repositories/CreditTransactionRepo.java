package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.CreditTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditTransactionRepo extends JpaRepository<CreditTransaction, Long> {
    Page<CreditTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
