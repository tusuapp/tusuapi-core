package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * SignUpVerification created by Rithik S(coderithik@gmail.com)
 **/

@Entity
@Table(name = "sign_up_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String emailOtp;

    private String phoneOtp;

    private boolean isEmailVerified;
    private boolean isPhoneVerified;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
