package com.tusuapp.coreapi.services.admin.tutors;

import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.TutorDetailsDto;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AdminTutorService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class AdminTutorService {

    private final TutorDetailRepo tutorDetailRepo;
    private final UserInfoRepo userInfoRepo;

    public ResponseEntity<?> getTutors() {
        List<TutorDetails> tutors = tutorDetailRepo.findAll();
        List<TutorDetailsDto> userDtos = tutors.stream().map(TutorDetailsDto::fromEntity).toList();
        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<?> approveTutor(Long tutorId) {
        User users = userInfoRepo.findById(tutorId).orElseThrow(() -> new EntityNotFoundException("Tutor not found"));
        users.setConfirmed(true);
        userInfoRepo.save(users);
        return ResponseEntity.ok().build();
    }

}
