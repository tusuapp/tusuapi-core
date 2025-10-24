package com.tusuapp.coreapi.models.dtos.accounts;

import com.tusuapp.coreapi.models.Country;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.CountryDto;
import lombok.Data;


@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private CountryDto country;
    private String timeZone;
    private Boolean isActive;
    private String fullName;
    private String imageUrl;
    private Role role;
    private boolean isEmailVerified;
    private String timeZoneOffset;
    private boolean isCompleteProfile;
    private String address;



    public static UserDto fromUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        if(user.getCountry() !=null) {
            dto.setCountry(CountryDto.fromCountry(user.getCountry()));
        }
        dto.setTimeZone(user.getTimeZone());
        dto.setFullName(user.getFullName());
        dto.setIsActive(user.getIsActive());
        dto.setImageUrl(user.getImageUrl());
        dto.setEmailVerified(user.getIsEmailVerified());
        dto.setTimeZoneOffset(user.getTimeZoneOffset());
        dto.setAddress(user.getAddress());
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
