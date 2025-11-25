package com.tusuapp.coreapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tutor_details")
@Getter
@Setter
@ToString
public class TutorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer experience;

    @Column(name = "hourly_charge")
    private Double hourlyCharge;

    private String gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

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
    private List<LanguageLocale> languages;

}
