package com.tusuapp.coreapi.services.classes;

import com.tusuapp.coreapi.models.BookingRequest;
import com.tusuapp.coreapi.repositories.BookingRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * ClassesService created by Rithik S(coderithik@gmail.com)
 **/
public class ClassesService {

    @Autowired
    private BookingRequestRepo bookingRequestRepo;

    public List<BookingRequest> getBookingRequests() {
        return bookingRequestRepo.findAll();
    }

}
