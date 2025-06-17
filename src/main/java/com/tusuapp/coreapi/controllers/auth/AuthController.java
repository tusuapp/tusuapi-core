package com.tusuapp.coreapi.controllers.auth;


import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.accounts.UserDto;
import com.tusuapp.coreapi.repositories.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;
import static com.tusuapp.coreapi.utils.SessionUtil.isStudent;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @GetMapping("/user")
    public ResponseEntity<?> getAuthUser() {
        User user = userInfoRepo.findById(getCurrentUserId()).get();
        UserDto userDto = UserDto.fromUser(user);
        userDto.setRole(new UserDto.Role(isStudent() ? 3 : 4));
        return ResponseEntity.ok(userDto);
    }

}
