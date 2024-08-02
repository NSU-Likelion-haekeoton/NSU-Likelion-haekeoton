package com.example.medichart.OAuth.controller;


import com.example.medichart.OAuth.dto.CustomOAuth2User;
import com.example.medichart.OAuth.dto.UserProfile;
import com.example.medichart.OAuth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserProfile getProfile(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        String username = user.getUsername();

        // 사용자의 프로필 정보를 가져오는 서비스 호출
        return userService.getUserProfile(username);
    }
}