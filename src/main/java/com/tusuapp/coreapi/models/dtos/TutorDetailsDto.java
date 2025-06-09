package com.tusuapp.coreapi.models.dtos;

import com.tusuapp.coreapi.models.TutorDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class TutorDetailsDto {

    private Long id;
    private Long userId;
    private String description;
    private Integer experience;
    private BigDecimal hourlyCharge;
    private String gender;
    private Integer createdBy;
    private Integer updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TutorDetailsDto fromEntity(TutorDetails entity) {
        TutorDetailsDto dto = new TutorDetailsDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setDescription(entity.getDescription());
        dto.setExperience(entity.getExperience());
        dto.setHourlyCharge(entity.getHourlyCharge());
        dto.setGender(entity.getGender());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
