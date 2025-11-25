package com.tusuapp.coreapi.controllers.admin;

import com.tusuapp.coreapi.models.dtos.auth.LoginDto;
import com.tusuapp.coreapi.services.admin.auth.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/admin/auth/login")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService authService;

    @PostMapping
    public ResponseEntity<?> loginAdmin(@RequestBody LoginDto loginDto){
        return authService.loginAdmin(loginDto);
    }
}
