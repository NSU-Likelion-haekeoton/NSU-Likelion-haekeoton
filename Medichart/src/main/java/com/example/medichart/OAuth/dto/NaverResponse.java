package com.example.medichart.OAuth.dto;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        // 네이버 응답의 "response" 필드를 추출
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        // 네이버 응답에서 "id" 필드를 사용하여 Provider ID를 추출
        Object id = attribute.get("id");
        if (id == null) {
            throw new IllegalArgumentException("Provider ID is missing");
        }
        return id.toString();
    }

    @Override
    public String getEmail() {
        // 네이버 응답에서 "email" 필드를 사용하여 이메일 주소를 추출
        Object email = attribute.get("email");
        if (email == null) {
            throw new IllegalArgumentException("Email is missing");
        }
        return email.toString();
    }

    @Override
    public String getName() {
        // 네이버 응답에서 "name" 필드를 사용하여 사용자 이름을 추출
        Object name = attribute.get("name");
        if (name == null) {
            throw new IllegalArgumentException("Name is missing");
        }
        return name.toString();
    }
}
