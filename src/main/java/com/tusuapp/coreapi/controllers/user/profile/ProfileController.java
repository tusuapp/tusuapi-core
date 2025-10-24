package com.tusuapp.coreapi.controllers.user.profile;

import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UpdateProfileDto;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.services.user.profile.ProfileService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.tusuapp.coreapi.utils.SessionUtil.*;


@RestController
@RequestMapping("/user/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/total-classes")
    private ResponseEntity<?> getUserTotalClasses(){
        return profileService.getUserTotalClassesCount();
    }

    @GetMapping("/tutor")
    public ResponseEntity<?> getTutorProfile(){
        return profileService.getTutorProfile();
    }

    @GetMapping("/tutor/{id}")
    public ResponseEntity<?> getTutorProfileWithId(@PathVariable("id") Long id){
        return profileService.getTutorProfileWithId(id);
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

    @GetMapping("/me")
    public ResponseEntity<?> getAuthUser() {
        return profileService.getCurrentUser();
    }


}
