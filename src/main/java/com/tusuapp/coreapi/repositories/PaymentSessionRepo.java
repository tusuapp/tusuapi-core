package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.PaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSessionRepo extends JpaRepository<PaymentSession,String> {
}
