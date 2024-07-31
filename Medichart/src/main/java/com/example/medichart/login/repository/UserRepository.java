package com.example.medichart.login.repository;

import com.example.medichart.login.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByEmailVerifiedFalseAndCreatedAtBefore(Date cutoff);

    @Query("SELECT u.email FROM UserEntity u WHERE u.name = :name AND FUNCTION('DATE', u.birthdate) = FUNCTION('DATE', :birthdate)")
    List<String> findEmailsByNameAndBirthdate(@Param("name") String name, @Param("birthdate") Date birthdate);

    // 최신순으로 정렬된 사용자 목록을 반환
    List<UserEntity> findAllByOrderByCreatedAtDesc();
}
