package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_wallets", schema = "tusu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "uid")
    private Long userId;

    @Column(nullable = false)
    private double balance;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



}
