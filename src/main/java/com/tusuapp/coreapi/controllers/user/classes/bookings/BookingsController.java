package com.tusuapp.coreapi.controllers.user.classes.bookings;

import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.services.user.classes.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * BookingsController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/user/classes/bookings")
public class BookingsController {

    @Autowired
    private ClassesService classesService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateBooking(@RequestBody InitiateBookingReqDto initiateBookingReqDto){
        try {
            return classesService.initiateBooking(initiateBookingReqDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(e);
        }
    }



    @PostMapping("/pay")
    public ResponseEntity<?> purchaseClass(@RequestParam Long bookingId){
        try {
            return classesService.purchaseClass(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(e);
        }
    }

}
