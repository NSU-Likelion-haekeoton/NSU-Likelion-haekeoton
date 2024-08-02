package com.example.medichart.OAuth.service;

import com.example.medichart.OAuth.dto.NaverResponse;
import org.springframework.beans.factory.annotation.Value;  // 올바른 @Value 어노테이션 올바른 방법이다.
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NaverOAuth2Service {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String accessTokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;

    public NaverResponse getUserInfo(String code, String state) {
        String accessToken = getAccessToken(code, state);

        // 사용자 정보를 가져오기 위해 RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();
        String url = userInfoUri;

        // 요청 헤더에 액세스 토큰을 포함
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 사용자 정보 요청
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return new NaverResponse(response.getBody());
    }

    private String getAccessToken(String code, String state) {
        // 액세스 토큰을 요청하는 URL
        String url = String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s&redirect_uri=%s&code=%s&state=%s",
                accessTokenUri, clientId, clientSecret, redirectUri, code, state);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        // 응답에서 액세스 토큰 추출
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            return (String) responseBody.get("access_token");
        }

        throw new RuntimeException("Failed to obtain access token");
    }
}
