package com.example.medichart.OAuth.repository;

import com.example.medichart.OAuth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    // 주어진 날짜 범위 내에서 가입자 수를 계산합니다.
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // 최신순으로 정렬된 사용자 목록을 반환

}