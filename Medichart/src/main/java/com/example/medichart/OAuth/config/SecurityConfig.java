package com.example.medichart.OAuth.config;

import com.example.medichart.OAuth.jwt.JWTFilter;
import com.example.medichart.OAuth.jwt.JWTUtil;
import com.example.medichart.OAuth.oauth2.CustomSuccessHandler;
import com.example.medichart.OAuth.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()));

        // CSRF 비활성화
        http
                .csrf(csrf -> csrf.disable());

        // 폼 로그인 비활성화
        http
                .formLogin(form -> form.disable());

        // HTTP Basic 인증 비활성화
        http
                .httpBasic(basic -> basic.disable());

        // JWT 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 설정
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler));

        // 경로별 인가 설정
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll() // 루트 경로는 모두 허용
                        .requestMatchers("/profile/**").authenticated() // /profile 경로는 인증된 사용자만 허용
                        .requestMatchers("/user").authenticated() // /user 경로는 인증된 사용자만 허용
                        .anyRequest().denyAll()); // 기본적으로 모든 요청 거부

        // 세션 설정 : STATELESS
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용된 출처 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용된 메서드 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 허용된 헤더 설정
        configuration.setAllowCredentials(true); // 자격 증명 허용
        return request -> configuration;
    }
}
