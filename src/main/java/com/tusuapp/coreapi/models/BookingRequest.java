package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity(name = "tutor_booking_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "tutor_id")
    private Long tutorId;

    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "from_datetime")
    private LocalDateTime fromDateTime;

    @Column(name = "to_datetime", nullable = false)
    private LocalDateTime toDateTime;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "commission_amount")
    private Double commissionAmount;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "status", nullable = false)
    private String status;
}
