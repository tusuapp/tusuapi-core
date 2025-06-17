package com.tusuapp.coreapi.controllers.user.classes;


import com.tusuapp.coreapi.services.user.classes.ClassesService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/classes")
public class ClassesController {


    @Autowired
    private ClassesService classesService;


    @GetMapping
    public ResponseEntity<?> getUserBookings(@RequestParam String types, @RequestParam(required = false) Integer limit){
        return classesService.getUserClasses(types,limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetails(@PathVariable("id") Long id){
        return classesService.getBookingDetails(id);
    }


}
