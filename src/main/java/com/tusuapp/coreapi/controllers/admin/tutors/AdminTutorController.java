package com.tusuapp.coreapi.controllers.admin.tutors;

import com.tusuapp.coreapi.services.admin.tutors.AdminTutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * AdminTutorController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/admin/tutors")
@RequiredArgsConstructor
public class AdminTutorController {

    private final AdminTutorService tutorService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTutors(){
        return tutorService.getTutors();
    }

    @PostMapping("/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> approveTutor(@RequestParam("tutorId") Long tutorId){
        return tutorService.approveTutor(tutorId);
    }
}
