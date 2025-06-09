package com.tusuapp.coreapi.models.dtos.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tusuapp.coreapi.models.User;
import lombok.Data;


@Data
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private Long phone;

    @JsonProperty("country")
    private Integer country;

    @JsonProperty("timezone")
    private String timeZone;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("full_name")
    private String fullName;

    public static UserDto fromUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCountry(user.getCountry());
        dto.setTimeZone(user.getTimeZone());
        dto.setFullName(user.getFullName());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}
