package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tutor_details")
public class TutorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer experience;

    @Column(name = "hourly_charge")
    private Double hourlyCharge;

    private String gender;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToMany
    @JoinTable(
            name = "categories_tutor_details__tutor_details_subject_ids",
            joinColumns = @JoinColumn(name = "tutor-detail_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> subjects;

    @ManyToMany
    @JoinTable(
            name = "categories_tutor_details_ds__tutor_details_discipline_ids",
            joinColumns = @JoinColumn(name = "tutor-detail_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> disciplines;

    @ManyToMany
    @JoinTable(
            name = "locales_tutor_details__tutor_details_languages",
            joinColumns = @JoinColumn(name = "tutor-detail_id"),
            inverseJoinColumns = @JoinColumn(name = "locale_id")
    )
    private List<Locale> languages;

}
