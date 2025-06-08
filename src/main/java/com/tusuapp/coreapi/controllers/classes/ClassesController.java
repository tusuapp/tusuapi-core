package com.tusuapp.coreapi.controllers.classes;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classes")
public class ClassesController {


    @GetMapping
    public ResponseEntity<?> getClasses(){

        return  null;
    }

}
