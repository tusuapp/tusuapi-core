package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * TusuAdmin created by Rithik S(coderithik@gmail.com)
 **/
@Entity
@Table(name = "admins")
@Getter
@Setter
public class TusuAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;
    private String contact;
    private String password;
    private String profileImageUrl;
    private String timeZone;

    private Instant lastOnline;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;

}
