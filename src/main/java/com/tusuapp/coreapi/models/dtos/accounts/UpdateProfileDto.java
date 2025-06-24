package com.tusuapp.coreapi.models.dtos.accounts;

import lombok.Data;
import java.util.List;

@Data
public class UpdateProfileDto {

    private String fullName;
    private String email;
    private Long phone;
    private String description;
    private Integer countryId;
    private Integer countryCode;
    private String timezone;
    private Integer experience;
    private String address;
    private List<Long> subjects;
    private List<Long> languages;
    private List<Long> disciplines;
    private Double hourlyCharge;
}
