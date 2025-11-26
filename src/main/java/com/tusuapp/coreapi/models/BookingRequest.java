package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id")
    private User tutor;


    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status")
    private String status; // e.g., requested, scheduled, completed, cancelled

    @Column(name = "is_rescheduled")
    private Boolean isRescheduled = false;

    @Column(name = "parent_booking_id")
    private Long parentBookingId;

    @Column(name = "reschedule_reason", columnDefinition = "TEXT")
    private String rescheduleReason;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "student_message", columnDefinition = "TEXT")
    private String studentMessage;

    @Column(name = "rescheduled_at")
    private LocalDateTime rescheduledAt;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "commission_amount")
    private double commissionAmount;

    @Column(name = "hourly_charge")
    private double hourlyCharge;

    @Column(name = "currency")
    private String currency;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Double getTotalAmount(){
        return this.commissionAmount + this.hourlyCharge;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
