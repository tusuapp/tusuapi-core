package com.tusuapp.coreapi.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "payment_session")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentSession {

    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Column(unique = true, name = "stripe_session_id")
    private String stripeSessionId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "booking_request_id")
    private Long bookingRequestId;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "tutor_id")
    private Long tutorId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
