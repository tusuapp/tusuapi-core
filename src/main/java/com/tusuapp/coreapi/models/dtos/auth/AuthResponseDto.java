package com.tusuapp.coreapi.models.dtos.auth;

import lombok.Data;

import java.time.Instant;

/**
 * AuthResponseDto created by Rithik S(coderithik@gmail.com)
 **/
@Data
public class AuthResponseDto {

    private String jwt;
    private Instant createdAt;

}
