package com.tusuapp.coreapi.models.dtos.auth;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role;
    private int countryId;
    private String timezone;
    private String provider = "local";
    private String loginAccess = "normal"; // or "social-login"
}