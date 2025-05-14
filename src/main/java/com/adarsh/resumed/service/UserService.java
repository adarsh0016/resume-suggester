package com.adarsh.resumed.service;

import com.adarsh.resumed.DTO.Users;
import com.adarsh.resumed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users register(String username, String password) {
        if(userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateDate(LocalDateTime.now());
        user.setIsActive(true);
        return userRepository.save(user);
    }
}
