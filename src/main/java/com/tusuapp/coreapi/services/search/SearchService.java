package com.tusuapp.coreapi.services.search;

import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tusuapp.coreapi.utils.SessionUtil.ROLE_TUTOR_ID;

/**
 * SearchService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserInfoRepo userRepo;


    public ResponseEntity<?> searchTutor(String searchKey) {
        List<User> tutors = userRepo.searchTutors(searchKey, ROLE_TUTOR_ID);
        List<UserDto> tutorsDtos = tutors.stream().map(UserDto::fromUser).toList();
        return ResponseEntity.ok(tutorsDtos);
    }


}
