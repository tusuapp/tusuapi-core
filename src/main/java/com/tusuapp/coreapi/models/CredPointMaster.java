package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cred_point_master", schema = "tusu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredPointMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "uid")
    private Long studentId;

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
