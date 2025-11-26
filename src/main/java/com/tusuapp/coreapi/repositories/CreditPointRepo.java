package com.tusuapp.coreapi.repositories;


import com.tusuapp.coreapi.models.UserWallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditPointRepo extends JpaRepository<UserWallet,Long> {
    Optional<UserWallet> findByUserId(Long studentId);
}
