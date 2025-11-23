package com.tusuapp.coreapi.models.dtos.auth;

import lombok.Data;

/**
 * ResetPasswordDto created by Rithik S(coderithik@gmail.com)
 **/
@Data
public class ResetPasswordDto {
    String token;
    String password;
    String confirmPassword;

}
