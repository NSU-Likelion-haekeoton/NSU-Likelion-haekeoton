package com.example.medichart.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private Long userId;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotNull(message = "생년월일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    private String status;
    private String loginMethod; // 로그인 방법 (예: '일반', '카카오', '네이버', '구글')

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private String role;
    private Boolean emailVerified;
}
