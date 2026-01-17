package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletRepo extends JpaRepository<UserWallet, Integer> {
    Optional<UserWallet> findByUserId(Long userId);
}
