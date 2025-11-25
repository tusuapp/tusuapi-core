package com.tusuapp.coreapi.services.admin.auth;

import com.google.gson.JsonObject;
import com.tusuapp.coreapi.models.TusuAdmin;
import com.tusuapp.coreapi.models.dtos.auth.AuthResponseDto;
import com.tusuapp.coreapi.models.dtos.auth.LoginDto;
import com.tusuapp.coreapi.repositories.TusuAdminRepo;
import com.tusuapp.coreapi.services.auth.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.tusuapp.coreapi.utils.ResponseUtil.errorResponse;

/**
 * AdminAuthService created by Rithik S(coderithik@gmail.com)
 **/
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final TusuAdminRepo adminRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public ResponseEntity<?> loginAdmin(LoginDto loginDto) {
        TusuAdmin admin = adminRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        if (!encoder.matches(loginDto.getPassword(), admin.getPassword())) {
            return errorResponse(HttpStatus.BAD_REQUEST,"Invalid credentials");
        }

        String jwt = jwtService.generateToken(admin.getId().toString(), admin.getEmail());
        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setJwt(jwt);
        responseDto.setCreatedAt(Instant.now());
        return ResponseEntity.ok(responseDto);
    }

}
