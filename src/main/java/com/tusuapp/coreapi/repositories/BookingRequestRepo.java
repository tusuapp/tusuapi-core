package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRequestRepo extends JpaRepository<BookingRequest,Long> {
}
