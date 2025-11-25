package com.tusuapp.coreapi.services.admin.tutors;

import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.TutorDetailsDto;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.repositories.TutorDetailRepo;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import com.tusuapp.coreapi.services.auth.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tusuapp.coreapi.constants.AccountConstants.ROLE_TUTOR;

/**
 * AdminTutorService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class AdminTutorService {

    private final TutorDetailRepo tutorDetailRepo;
    private final UserInfoRepo userInfoRepo;

    public ResponseEntity<?> getTutors(){
        List<TutorDetails> tutors = tutorDetailRepo.findAll();
        List<TutorDetailsDto> tutorDtos = tutors.stream().map(TutorDetailsDto::fromEntity).toList();
        return ResponseEntity.ok(tutorDtos);
    }

}
