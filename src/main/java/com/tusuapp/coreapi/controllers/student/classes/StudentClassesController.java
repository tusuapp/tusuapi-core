package com.tusuapp.coreapi.controllers.student.classes;


import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.models.dtos.payments.MakePaymentDto;
import com.tusuapp.coreapi.services.user.classes.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/classes")
public class StudentClassesController {

    @Autowired
    private ClassesService classesService;

    @GetMapping("upcoming")
    public ResponseEntity<?> getUpcomingClasses(){
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> initiateBooking(@RequestBody InitiateBookingReqDto initiateBookingReqDto){
        return classesService.initiateBooking(initiateBookingReqDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetails(@PathVariable String id){
        return classesService.getClassDetails(id);
    }

    @PutMapping
    public ResponseEntity<?> makePayment(@RequestBody MakePaymentDto makePaymentDto){
        return null;
    }

}
