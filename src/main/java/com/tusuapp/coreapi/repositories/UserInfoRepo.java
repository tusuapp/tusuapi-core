package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Use 'email' if that is the correct field for login
}