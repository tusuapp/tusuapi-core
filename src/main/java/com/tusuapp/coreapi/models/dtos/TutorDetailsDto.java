package com.tusuapp.coreapi.models.dtos;

import com.tusuapp.coreapi.models.LanguageLocale;
import com.tusuapp.coreapi.models.TutorDetails;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.dropdowns.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorDetailsDto {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private boolean approved;
    private String userImageUrl;
    private String description;
    private Integer experience;
    private Double hourlyCharge;
    private boolean confirmed;
    private String gender;
    private List<CategoryDto> subjects;
    private List<CategoryDto> disciplines;
    private List<LanguageLocale> languages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TutorDetailsDto fromEntity(TutorDetails entity) {
        if (entity == null) {
            return null;
        }
        TutorDetailsDto dto = new TutorDetailsDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setExperience(entity.getExperience());
        dto.setHourlyCharge(entity.getHourlyCharge());
        dto.setGender(entity.getGender());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        if (entity.getUser() != null) {
            User user = entity.getUser();
            dto.setUserId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setUserImageUrl(user.getImageUrl());
        }
        if (entity.getSubjects() != null) {
            List<CategoryDto> subjectDtos = entity.getSubjects().stream().map(sub -> {
                CategoryDto catDto = new CategoryDto();
                catDto.setId(sub.getId());
                catDto.setName(sub.getName());
                return catDto;
            }).toList();
            dto.setSubjects(subjectDtos);
        } else {
            dto.setSubjects(Collections.emptyList());
        }
        if (entity.getDisciplines() != null) {
            List<CategoryDto> disciplineDtos = entity.getDisciplines().stream().map(disc -> {
                CategoryDto catDto = new CategoryDto();
                catDto.setId(disc.getId());
                catDto.setName(disc.getName());
                return catDto;
            }).toList();
            dto.setDisciplines(disciplineDtos);
        } else {
            dto.setDisciplines(Collections.emptyList());
        }
        if (entity.getLanguages() != null) {
            List<LanguageLocale> languageDtos = entity.getLanguages().stream().map(lang -> {
                LanguageLocale langDto = new LanguageLocale();
                langDto.setId(lang.getId());
                langDto.setName(lang.getName());
                return langDto;
            }).toList();
            dto.setLanguages(languageDtos);
        } else {
            dto.setLanguages(Collections.emptyList());
        }
        return dto;
    }

    public static TutorDetailsDto fromUser(User user) {
        if (user == null) {
            return null;
        }
        TutorDetailsDto dto = new TutorDetailsDto();
        dto.setId(user.getId());
        dto.setUserId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setUserImageUrl(user.getImageUrl());
        dto.setConfirmed(user.getConfirmed());
        if (user.getTutorDetails() != null) {
            dto.setApproved(true);
        }
        return dto;
    }
}