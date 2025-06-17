package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepo extends JpaRepository<Subject,Long> {
}
