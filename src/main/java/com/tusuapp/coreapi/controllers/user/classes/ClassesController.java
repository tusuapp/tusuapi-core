package com.tusuapp.coreapi.controllers.user.classes;


import com.tusuapp.coreapi.models.dtos.bookings.InitiateBookingReqDto;
import com.tusuapp.coreapi.models.dtos.payments.MakePaymentDto;
import com.tusuapp.coreapi.services.user.classes.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/classes")
public class ClassesController {


    @Autowired
    private ClassesService classesService;

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingClasses(){
        return ResponseEntity.ok().build();
    }




    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetails(@PathVariable String id){
        return classesService.getClassDetails(id);
    }

    public ResponseEntity<?> makePayment(@RequestBody MakePaymentDto makePaymentDto){
        return null;
    }

}
