

package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.RescheduleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRescheduleRepo extends JpaRepository<RescheduleRequest, Long> {
    List<RescheduleRequest> findAllByTutorIdAndStatus(Long tutorId, String isAccepted);
    List<RescheduleRequest> findAllByStudentIdAndStatus(Long tutorId, String isAccepted);
    List<RescheduleRequest> findAllByBookingIdAndStatus(Long bookingId, String isAccepted);

}
