package com.tusuapp.coreapi.repositories;


import com.tusuapp.coreapi.models.CredPointMaster;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CreditPointRepo extends JpaRepository<CredPointMaster,Long> {
    Optional<CredPointMaster> findByStudentId(Long studentId);
}
