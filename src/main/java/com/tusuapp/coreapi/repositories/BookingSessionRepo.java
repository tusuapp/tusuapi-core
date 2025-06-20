package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.models.BookingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingSessionRepo extends JpaRepository<BookingSession,Long> {
    boolean existsByBooking(BookingRequest bookingRequest);
    Optional<BookingSession> findByBooking_Id(Long bookingId);
}
