package com.example.demo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String getEmailById(String username) {
        return userRepository.findFirstByUsername(username)
                .map(User::getEmail)
                .orElse(null);
    }
}