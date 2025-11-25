package com.tusuapp.coreapi.controllers.admin.tutors;

import com.tusuapp.coreapi.services.admin.tutors.AdminTutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AdminTutorController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/admin/tutors")
@RequiredArgsConstructor
public class AdminTutorController {

    private final AdminTutorService tutorService;


    @GetMapping
    public ResponseEntity<?> getTutors(){
        return tutorService.getTutors();
    }
}
