package com.example.medichart.admin.service;

import com.example.medichart.OAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserStatisticsService {

    private final UserRepository userRepository;

    @Autowired
    public UserStatisticsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 오늘 가입한 사용자 수를 반환하는 메서드
    public long getTodaySignupCount() {
        LocalDate today = LocalDate.now(); // 현재 날짜를 얻음
        Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()); // 하루의 시작 시간
        Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()); // 하루의 끝 시간
        return userRepository.countByCreatedAtBetween(startOfDay, endOfDay); // 주어진 기간 내에 생성된 사용자 수를 반환
    }

    // 최근 7일 동안의 가입자 수를 리스트로 반환하는 메서드
    public List<Long> getLast7DaysSignupCounts() {
        return getSignupCountsForDays(7); // 7일 동안의 데이터를 반환
    }

    // 최근 12개월 동안의 가입자 수를 리스트로 반환하는 메서드
    public List<Long> getLast12MonthsSignupCounts() {
        return getSignupCountsForMonths(12); // 12개월 동안의 데이터를 반환
    }

    // 최근 3년 동안의 가입자 수를 리스트로 반환하는 메서드
    public List<Long> getLast3YearsSignupCounts() {
        return getSignupCountsForYears(3); // 3년 동안의 데이터를 반환
    }

    // 주어진 일 수 동안의 가입자 수를 리스트로 반환하는 메서드
    private List<Long> getSignupCountsForDays(int days) {
        LocalDate today = LocalDate.now(); // 현재 날짜
        return IntStream.range(0, days) // 0부터 days까지의 스트림 생성
                .mapToObj(i -> today.minusDays(i)) // 각 날짜를 계산
                .map(date -> {
                    Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()); // 하루의 시작 시간
                    Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()); // 하루의 끝 시간
                    return userRepository.countByCreatedAtBetween(startOfDay, endOfDay); // 주어진 기간 동안의 가입자 수를 반환
                })
                .collect(Collectors.toList()); // 리스트로 수집
    }

    // 주어진 월 수 동안의 가입자 수를 리스트로 반환하는 메서드
    private List<Long> getSignupCountsForMonths(int months) {
        LocalDate today = LocalDate.now(); // 현재 날짜
        return IntStream.range(0, months) // 0부터 months까지의 스트림 생성
                .mapToObj(i -> today.minusMonths(i)) // 각 월을 계산
                .map(date -> {
                    Date startOfMonth = Date.from(date.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()); // 월의 시작 시간
                    Date endOfMonth = Date.from(date.plusMonths(1).withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()); // 월의 끝 시간
                    return userRepository.countByCreatedAtBetween(startOfMonth, endOfMonth); // 주어진 기간 동안의 가입자 수를 반환
                })
                .collect(Collectors.toList()); // 리스트로 수집
    }

    // 주어진 연 수 동안의 가입자 수를 리스트로 반환하는 메서드
    private List<Long> getSignupCountsForYears(int years) {
        LocalDate today = LocalDate.now(); // 현재 날짜
        return IntStream.range(0, years) // 0부터 years까지의 스트림 생성
                .mapToObj(i -> today.minusYears(i)) // 각 년을 계산
                .map(date -> {
                    Date startOfYear = Date.from(date.withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant()); // 년의 시작 시간
                    Date endOfYear = Date.from(date.plusYears(1).withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant()); // 년의 끝 시간
                    return userRepository.countByCreatedAtBetween(startOfYear, endOfYear); // 주어진 기간 동안의 가입자 수를 반환
                })
                .collect(Collectors.toList()); // 리스트로 수집
    }
}
