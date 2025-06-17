package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.TutorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TutorSlotRepo extends JpaRepository<TutorSlot,Long> {
    List<TutorSlot> findAllByTutorId(Long tutorId);

    @Query("SELECT s FROM TutorSlot s WHERE s.tutorId = :tutorId " +
                  "AND s.fromDatetime < :newTo AND s.toDatetime > :newFrom")
    List<TutorSlot> findOverlappingSlots(@Param("tutorId") Long tutorId,
                                         @Param("newFrom") LocalDateTime newFrom,
                                         @Param("newTo") LocalDateTime newTo);
}
