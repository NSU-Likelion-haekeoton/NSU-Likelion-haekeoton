package com.example.medichart.admin.service;

import com.example.medichart.login.repository.UserRepository;
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

    public long getTodaySignupCount() {
        LocalDate today = LocalDate.now();
        Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return userRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    }

    public List<Long> getLast7DaysSignupCounts() {
        return getSignupCountsForDays(7);
    }

    public List<Long> getLast12MonthsSignupCounts() {
        return getSignupCountsForMonths(12);
    }

    public List<Long> getLast3YearsSignupCounts() {
        return getSignupCountsForYears(3);
    }

    private List<Long> getSignupCountsForDays(int days) {
        LocalDate today = LocalDate.now();
        return IntStream.range(0, days)
                .mapToObj(i -> today.minusDays(i))
                .map(date -> {
                    Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    return userRepository.countByCreatedAtBetween(startOfDay, endOfDay);
                })
                .collect(Collectors.toList());
    }

    private List<Long> getSignupCountsForMonths(int months) {
        LocalDate today = LocalDate.now();
        return IntStream.range(0, months)
                .mapToObj(i -> today.minusMonths(i))
                .map(date -> {
                    Date startOfMonth = Date.from(date.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date endOfMonth = Date.from(date.plusMonths(1).withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    return userRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
                })
                .collect(Collectors.toList());
    }

    private List<Long> getSignupCountsForYears(int years) {
        LocalDate today = LocalDate.now();
        return IntStream.range(0, years)
                .mapToObj(i -> today.minusYears(i))
                .map(date -> {
                    Date startOfYear = Date.from(date.withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date endOfYear = Date.from(date.plusYears(1).withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    return userRepository.countByCreatedAtBetween(startOfYear, endOfYear);
                })
                .collect(Collectors.toList());
    }
}
