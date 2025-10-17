package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tutor_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tutor_id")
    private Long tutorId;

    @Column(name = "status")
    private Boolean status = false;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(name = "from_datetime")
    private LocalDateTime fromDatetime;

    @Column(name = "to_datetime")
    private LocalDateTime toDatetime;

    @Column(name = "trial_slot")
    private boolean isTrialSlot;

}
