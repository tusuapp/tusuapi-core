package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.CreditTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CreditTransactionRepo extends JpaRepository<CreditTransaction, Long> {

    Page<CreditTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<CreditTransaction> findByUserIdAndDescriptionOrderByCreatedAtDesc(Long userId, String description, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM CreditTransaction t WHERE t.userId = :userId AND t.description = :description")
    Double sumAmountByUserIdAndDescription(@Param("userId") Long userId, @Param("description") String description);
}
