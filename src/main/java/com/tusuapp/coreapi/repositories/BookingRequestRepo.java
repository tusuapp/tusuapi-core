package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.BookingRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRequestRepo extends JpaRepository<BookingRequest,Long> {
    List<BookingRequest> findAllByStudentIdAndStatus(Long studentId, String status);
    List<BookingRequest> findAllByTutorIdAndStatus(Long tutorId, String status);
    List<BookingRequest> findAllByStudentIdAndStatusIn(Long studentId, List<String> statusList, Pageable pageable);
    List<BookingRequest> findAllByTutorIdAndStatusIn(Long tutorId, List<String> statusList, Pageable pageable);
    long countByStudentIdAndStatus(Long studentId, String status);
    List<BookingRequest> findAllByStartTimeBeforeAndStatusIn(LocalDateTime time, List<String> statuses);
    @Query("SELECT b FROM BookingRequest b WHERE b.status = 'accepted' AND b.startTime BETWEEN :now AND :nowPlus15")
    List<BookingRequest> findAllAcceptedBookingsWithinNext15Minutes(@Param("now") LocalDateTime now, @Param("nowPlus15") LocalDateTime nowPlus15);
    List<BookingRequest> findByTutorIdAndStatusInAndStartTimeAfterOrderByStartTimeAsc(Long tutorId, List<String> statuses, LocalDateTime startTime);
    List<BookingRequest> findAllByStatusAndEndTimeLessThanEqual(String status, LocalDateTime time);


}
