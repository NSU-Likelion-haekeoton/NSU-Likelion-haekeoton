package com.example.medichart.login.service;

import com.example.medichart.login.dto.OAuthAttributes;
import com.example.medichart.login.entity.UserEntity;
import com.example.medichart.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService를 사용하여 사용자 정보를 가져옵니다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 로그인 제공자 및 사용자 정보에 따라 사용자 속성을 추출합니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 사용자 속성을 OAuthAttributes 객체로 변환합니다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 사용자 정보를 데이터베이스에 저장하거나 업데이트합니다.
        UserEntity user = saveOrUpdate(attributes, registrationId);

        // OAuth2User 객체를 반환합니다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private UserEntity saveOrUpdate(OAuthAttributes attributes, String registrationId) {
        // 이메일로 사용자 검색
        return userRepository.findByEmail(attributes.getEmail())
                .map(entity -> updateUser(entity, attributes))
                .orElseGet(() -> createUser(attributes, registrationId));
    }

    private UserEntity updateUser(UserEntity user, OAuthAttributes attributes) {
        // 기존 사용자 정보 업데이트
        user.setName(attributes.getName());
        return userRepository.save(user);
    }

    private UserEntity createUser(OAuthAttributes attributes, String registrationId) {
        // 새 사용자 생성
        UserEntity userEntity = attributes.toEntity(registrationId);
        return userRepository.save(userEntity);
    }
}
