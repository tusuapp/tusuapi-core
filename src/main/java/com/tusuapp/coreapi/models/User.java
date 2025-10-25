package com.tusuapp.coreapi.models;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "User")
@Table(name = "users-permissions_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String provider;
    private String password;
    private Boolean confirmed;
    private Boolean blocked;
    private Integer role;
    private String phone;
    @Column(name = "is_mobile_verified")
    private Boolean isMobileVerified;
    @Column(name = "fullname")
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country", referencedColumnName = "id")
    private Country country;

    @Column(name = "tutor_details")
    private Integer tutorDetails;
    @Column(name = "timezone")
    private String timeZone;
    @Column(name = "timezone_offset")
    private String timeZoneOffset;
    private String address;
    @Column(name = "social_login")
    private Integer socialLogin;
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;
    private String uuid;
    @Column(name = "users_chat_register")
    private Integer usersChatRegister;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "permanent_delete")
    private Boolean permanentDelete;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "image_url")
    private String imageUrl;
}
