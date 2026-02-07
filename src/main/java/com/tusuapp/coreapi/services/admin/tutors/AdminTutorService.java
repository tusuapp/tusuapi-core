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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * AdminTutorService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class AdminTutorService {

    private final TutorDetailRepo tutorDetailRepo;
    private final UserInfoRepo userInfoRepo;

    public ResponseEntity<?> getTutors(Boolean confirmed, Pageable pageable) {
        Page<TutorDetails> tutors;
        if (confirmed != null) {
            tutors = tutorDetailRepo.findByUserConfirmed(confirmed, pageable);
        } else {
            tutors = tutorDetailRepo.findAll(pageable);
        }
        Page<TutorDetailsDto> userDtos = tutors.map(TutorDetailsDto::fromEntity);
        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<?> getTutorById(Long id) {
        TutorDetails tutor = tutorDetailRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found with id: " + id));
        return ResponseEntity.ok(TutorDetailsDto.fromEntity(tutor));
    }

    public ResponseEntity<?> approveTutor(Long tutorId) {
        TutorDetails tutor = tutorDetailRepo.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found"));
        User users = tutor.getUser();
        users.setConfirmed(true);
        userInfoRepo.save(users);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> blockTutor(Long tutorId, Boolean block) {
        TutorDetails tutor = tutorDetailRepo.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found"));
        User users = tutor.getUser();
        users.setBlocked(block);
        userInfoRepo.save(users);
        return ResponseEntity.ok().build();
    }

}
