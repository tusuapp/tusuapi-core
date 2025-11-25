package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.TusuAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TusuAdminRepo extends JpaRepository<TusuAdmin, Long> {

    Optional<TusuAdmin> findByEmail(String email);

}
