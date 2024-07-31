package com.example.medichart.login.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/findid")
    public String findIdPage() {
        return "findIdPage";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required = false) String email,
                        @RequestParam(required = false) String password,
                        HttpServletResponse response,
                        HttpSession session) {
        if (email != null && password != null) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                session.setMaxInactiveInterval(1800); // 세션 타임아웃 30분

                Cookie userCookie = new Cookie("memberId", email);
                userCookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효 기간 7일
                userCookie.setPath("/"); // 전체 도메인에서 접근 가능
                response.addCookie(userCookie);

                return "redirect:/";
            } catch (Exception e) {
                return "Login failed: " + e.getMessage();
            }
        } else {
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("userEmail", "guest@example.com");

            return "redirect:/";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        Cookie userCookie = new Cookie("memberId", null);
        userCookie.setPath("/");
        userCookie.setMaxAge(0);
        response.addCookie(userCookie);

        return "redirect:/auth/login";
    }

    @GetMapping("/session")
    public String getSessionInfo(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String userEmail = (String) session.getAttribute("userEmail");

        if (isLoggedIn != null && isLoggedIn) {
            return "Logged in as: " + userEmail;
        } else {
            return "Not logged in";
        }
    }
}
