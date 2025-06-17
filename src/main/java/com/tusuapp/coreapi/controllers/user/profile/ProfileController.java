package com.tusuapp.coreapi.controllers.user.profile;

import com.tusuapp.coreapi.services.user.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;


    @GetMapping("/total-classes")
    private ResponseEntity<?> getUserTotalClasses(){
        return profileService.getUserTotalClassesCount();
    }

}
