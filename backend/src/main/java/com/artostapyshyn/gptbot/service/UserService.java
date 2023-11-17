package com.artostapyshyn.gptbot.service;

import com.artostapyshyn.gptbot.model.User;
import com.artostapyshyn.gptbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getAuthUser(Authentication authentication) {
        String studentEmail = authentication.getName();
        return userRepository.findByEmail(studentEmail);
    }

}
