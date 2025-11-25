package com.tusuapp.coreapi.controllers.admin;

import com.tusuapp.coreapi.models.dtos.auth.LoginDto;
import com.tusuapp.coreapi.services.admin.auth.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginDto loginDto){
        return authService.loginAdmin(loginDto);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifySession(){
        return ResponseEntity.ok().build();
    }
}
