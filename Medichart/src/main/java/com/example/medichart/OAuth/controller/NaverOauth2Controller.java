package com.example.medichart.OAuth.controller;

import com.example.medichart.OAuth.dto.NaverResponse;
import com.example.medichart.OAuth.service.NaverOAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth2/code/naver")
public class NaverOauth2Controller {

    @Autowired
    private NaverOAuth2Service naverOAuth2Service;

    @GetMapping
    public ResponseEntity<?> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        try {
            // 인증 코드로 네이버 사용자 정보 가져오기
            NaverResponse userResponse = naverOAuth2Service.getUserInfo(code, state);

            // 사용자 정보를 처리하고, 로그인 처리 등의 로직을 추가합니다.
            return ResponseEntity.ok(userResponse); // 필요한 정보를 클라이언트에 응답
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 처리 중 오류 발생");
        }
    }
}