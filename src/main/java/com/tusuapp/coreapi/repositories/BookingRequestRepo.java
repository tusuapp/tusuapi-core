package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRequestRepo extends JpaRepository<BookingRequest,Long> {
    List<BookingRequest> findAllByStudentIdAndStatus(Long studentId, String status);
    List<BookingRequest> findAllByTutorIdAndStatus(Long tutorId, String status);
    List<BookingRequest> findAllByStudentIdAndStatusIn(Long studentId, List<String> statusList);
    List<BookingRequest> findAllByTutorIdAndStatusIn(Long tutorId, List<String> statusList);
    long countByStudentIdAndStatus(Long studentId, String status);

}
