package com.example.medichart.login.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "User") // 테이블 이름을 "User"로 설정
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID_NO")
    private Long userId;

    @Column(name = "USER_EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "USER_PWD", length = 255)
    private String password;

    @Column(name = "USER_BIRTHDATE")
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column(name = "USER_STATUS", nullable = false, length = 255)
    private String status;

    @Column(name = "LOGIN_METHOD", nullable = false, length = 20)
    private String loginMethod;

    @Column(name = "USER_CREATED_AT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "USER_ROLE", nullable = false, length = 255)
    private String role;

    @Column(name = "EMAIL_VERIFIED", nullable = false)
    private Boolean emailVerified;

    @PrePersist
    public void prePersist() {
        if (status == null) status = "INACTIVE";
        if (role == null) role = "USER";
        if (createdAt == null) createdAt = new Date();
        if (emailVerified == null) emailVerified = false;
    }

    public boolean isEmailVerified() {
        return emailVerified != null && emailVerified;
    }
}
