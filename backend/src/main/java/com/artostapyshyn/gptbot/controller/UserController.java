package com.artostapyshyn.gptbot.controller;

import com.artostapyshyn.gptbot.model.User;
import com.artostapyshyn.gptbot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat-logs")
@CrossOrigin(maxAge = 3600, origins = "*")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) {
        User user = userService.getAuthUser(authentication);
        return ResponseEntity.ok(user);
    }

}
