package com.tusuapp.coreapi.controllers.user.profile;

import com.tusuapp.coreapi.models.dtos.accounts.UpdateProfileDto;
import com.tusuapp.coreapi.services.user.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/total-classes")
    private ResponseEntity<?> getUserTotalClasses(){
        return profileService.getUserTotalClassesCount();
    }

    @GetMapping("/tutor")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> getTutorProfile(){
        return profileService.getTutorProfile();
    }

    @PutMapping("/tutor")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    public ResponseEntity<?> updateTutorProfile(@RequestBody UpdateProfileDto updateProfileDto){
        return profileService.updateTutorProfile(updateProfileDto);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDto updateDto){
        return profileService.updateUserProfile(updateDto);
    }

}
