package com.example.medichart.login.service;

import com.example.medichart.login.dto.UserDTO;
import com.example.medichart.login.entity.UserEntity;
import com.example.medichart.login.entity.VerificationToken;
import com.example.medichart.login.enums.TokenType;
import com.example.medichart.login.repository.UserRepository;
import com.example.medichart.login.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final VerificationTokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일로 찾을 수 있는 사용자가 없습니다: " + email));

        if (!userEntity.isEmailVerified()) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다: " + email);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole())
                .build();
    }

    @Transactional
    public UserEntity registerUser(UserDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 비어 있거나 null입니다.");
        }

        // 로그인 방법이 null이거나 비어있는 경우 '일반'으로 기본값 설정
        String loginMethod = (userDTO.getLoginMethod() != null && !userDTO.getLoginMethod().isEmpty())
                ? userDTO.getLoginMethod() : "일반";

        // 이메일 중복 체크
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이메일이 이미 사용 중입니다.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setName(userDTO.getName());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setBirthdate(userDTO.getBirthdate());
        userEntity.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : "INACTIVE");
        userEntity.setLoginMethod(loginMethod);

        // LocalDateTime을 Date로 변환
        userEntity.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        userEntity.setRole(userDTO.getRole());
        userEntity.setEmailVerified(!loginMethod.equals("일반")); // 일반 회원가입의 경우 이메일 인증 필요

        UserEntity tempUser = userRepository.save(userEntity);

        if (loginMethod.equals("일반")) {
            // 이메일 인증 토큰 생성 및 전송
            String token = UUID.randomUUID().toString();
            saveVerificationToken(tempUser, token);
            sendVerificationEmail(tempUser.getEmail(), token);
        }

        return tempUser;
    }

    private void sendVerificationEmail(String email, String token) {
        String verificationUrl = "http://localhost:8080/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증");
        message.setText("다음 링크를 클릭하여 이메일을 인증하십시오: " + verificationUrl);
        mailSender.send(message);
    }

    private void saveVerificationToken(UserEntity user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30분 유효
        verificationToken.setTokenType(TokenType.EMAIL_VERIFICATION);
        tokenRepository.save(verificationToken);
    }

    public boolean verifyEmail(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    if (verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                        UserEntity user = verificationToken.getUser();
                        user.setEmailVerified(true);
                        userRepository.save(user);
                        tokenRepository.delete(verificationToken); // 사용한 토큰은 삭제
                        return true;
                    } else {
                        tokenRepository.delete(verificationToken); // 만료된 토큰은 삭제
                        return false;
                    }
                })
                .orElse(false);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void removeUnverifiedUsers() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(3); // 3시간 기준
        Date cutoff = Date.from(cutoffDate.atZone(ZoneId.systemDefault()).toInstant());
        List<UserEntity> unverifiedUsers = userRepository.findAllByEmailVerifiedFalseAndCreatedAtBefore(cutoff);
        for (UserEntity user : unverifiedUsers) {
            userRepository.delete(user);
        }
    }

    public void createPasswordResetTokenForUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일로 사용자를 찾을 수 없습니다: " + email));

        String token = UUID.randomUUID().toString();
        VerificationToken resetToken = new VerificationToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // 토큰의 유효 기간을 1시간으로 설정
        resetToken.setTokenType(TokenType.PASSWORD_RESET);

        tokenRepository.save(resetToken);
        sendPasswordResetEmail(email, token);
    }

    private void sendPasswordResetEmail(String email, String token) {
        String resetUrl = "http://localhost:8080/resetPassword?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 재설정");
        message.setText("다음 링크를 클릭하여 비밀번호를 재설정하십시오: " + resetUrl);
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        VerificationToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 토큰입니다: " + token));

        if (resetToken.getTokenType() != TokenType.PASSWORD_RESET) {
            throw new IllegalArgumentException("비밀번호 재설정 토큰이 아닙니다.");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

    public List<UserEntity> getAllUsersSortedByCreatedAtDesc() {
        return userRepository.findAllByOrderByCreatedAtDesc(); // 최신순으로 정렬된 사용자 목록 반환
    }

    public List<String> findEmailsByNameAndBirthdate(String name, Date birthdate) {
        // Date를 LocalDate로 변환
        LocalDate localBirthdate = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // LocalDate를 다시 Date로 변환
        Date sqlBirthdate = Date.from(localBirthdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return userRepository.findEmailsByNameAndBirthdate(name, sqlBirthdate);
    }
}
