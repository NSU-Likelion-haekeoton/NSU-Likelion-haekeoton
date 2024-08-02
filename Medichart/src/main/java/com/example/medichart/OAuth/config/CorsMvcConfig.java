package com.example.medichart.OAuth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/user/**") // WebSocket 엔드포인트 경로
                .allowedOrigins("http://localhost:3000") // 허용된 출처 설정
                .allowedMethods("GET", "POST") // 허용된 메서드 설정
                .allowedHeaders("Authorization", "Content-Type") // 허용된 헤더 설정
                .allowCredentials(true); // 자격 증명 허용
    }
}