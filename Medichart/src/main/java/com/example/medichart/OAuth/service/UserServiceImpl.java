package com.example.medichart.OAuth.service;


import com.example.medichart.OAuth.dto.UserProfile;
import com.example.medichart.OAuth.entity.UserEntity;
import com.example.medichart.OAuth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfile getUserProfile(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        // UserEntity를 UserProfile DTO로 변환
        return new UserProfile(
                userEntity.getUsername(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }
}