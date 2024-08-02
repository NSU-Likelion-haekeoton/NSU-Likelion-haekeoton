package com.example.medichart.OAuth.service;

import com.example.medichart.OAuth.dto.UserProfile;

public interface UserService {
    UserProfile getUserProfile(String username);
}
