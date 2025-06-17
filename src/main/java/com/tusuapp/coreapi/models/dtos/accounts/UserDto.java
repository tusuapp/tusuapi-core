package com.tusuapp.coreapi.models.dtos.accounts;

import com.tusuapp.coreapi.models.User;
import lombok.Data;


@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private Long phone;
    private Integer country;
    private String timeZone;
    private Boolean isActive;
    private String fullName;
    private String imageUrl;
    private Role role;
    private boolean isEmailVerified;
    private String timeZoneOffset;


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
        dto.setImageUrl(user.getImageUrl());
        dto.setEmailVerified(user.getIsEmailVerified());
        dto.setTimeZoneOffset(user.getTimeZoneOffset());
        return dto;
    }

    @Data
    public static class Role {
        int id;
        String name;
        String type;

        public Role(int code) {
            if (code == 3) {
                this.id = 3;
                this.name
                        = "Student";
                this.type = "student";
            }
            if(code == 4){
                this.id = 4;
                this.name
                        = "Tutor";
                this.type = "tutor";
            }
        }

    }

}
