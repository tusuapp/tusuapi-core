package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.TutorSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorSlotRepo extends JpaRepository<TutorSlot,Long> {
}
