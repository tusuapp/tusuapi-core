package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.TutorDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorDetailRepo extends JpaRepository<TutorDetails, Long> {
    Optional<TutorDetails> findByUserId(Long userId);

    Page<TutorDetails> findByUserConfirmed(Boolean confirmed, Pageable pageable);
}
