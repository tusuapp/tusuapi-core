package com.tusuapp.coreapi.controllers.user.bookings;

import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.models.dtos.bookings.ChangeBookingStatusDto;
import com.tusuapp.coreapi.models.dtos.bookings.RescheduleBookingDto;
import com.tusuapp.coreapi.services.user.bookings.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * BookingsController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/user/classes/bookings")
public class BookingsController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateBooking(@RequestBody InitiateBookingReqDto initiateBookingReqDto){
        try {
            return bookingService.initiateBooking(initiateBookingReqDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(e);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> purchaseClass(@RequestParam Long bookingId){
        try {
            return bookingService.purchaseClass(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserBookings(@RequestParam String types, @RequestParam(required = false) Integer limit){
        return bookingService.getUserClasses(types,limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetails(@PathVariable("id") Long id){
        return bookingService.getBookingDetails(id);
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> rejectClass(@RequestBody ChangeBookingStatusDto statusDto){
        return bookingService.changeBookingStatus(statusDto);
    }

}
