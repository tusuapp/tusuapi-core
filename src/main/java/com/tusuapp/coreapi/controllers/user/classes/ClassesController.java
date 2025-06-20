package com.tusuapp.coreapi.controllers.user.classes;


import com.tusuapp.coreapi.services.user.classes.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/classes")
public class ClassesController {


    @Autowired
    private ClassService classService;


    @PostMapping("/start")
    public ResponseEntity<?> startClass(@RequestParam("bookingId") Long bookingId) throws Exception {
        return classService.startClass(bookingId);
    }



}
