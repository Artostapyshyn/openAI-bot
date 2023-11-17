package com.artostapyshyn.gptbot.controller;

import com.artostapyshyn.gptbot.dto.LoginDto;
import com.artostapyshyn.gptbot.dto.SignUpDto;
import com.artostapyshyn.gptbot.model.User;
import com.artostapyshyn.gptbot.service.UserDetailsServiceImpl;
import com.artostapyshyn.gptbot.service.UserService;
import com.artostapyshyn.gptbot.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.artostapyshyn.gptbot.constants.ControllerConstants.MESSAGE;
import static com.artostapyshyn.gptbot.enums.Role.ROLE_ADMIN;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(maxAge = 3600, origins = "*")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginDto loginDto) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            User foundUser = userService.findByEmail(loginDto.email());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));
            userService.save(foundUser);
        } catch (BadCredentialsException e) {
            return handleUnauthorized();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.email());
        String token = jwtTokenUtil.generateToken(userDetails, loginDto.id());

        responseMap.put(MESSAGE, "Logged In");
        responseMap.put("token", token);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signUpUser(@RequestBody SignUpDto signUpDto) {
        Map<String, Object> responseMap = new HashMap<>();

        if (userService.existsByEmail(signUpDto.email())) {
            responseMap.put(MESSAGE, "User already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMap);
        }

        User user = createUserFromSignUpDto(signUpDto);
        userService.save(user);

        responseMap.put(MESSAGE, "User created");
        return ResponseEntity.ok(responseMap);
    }

    private User createUserFromSignUpDto(SignUpDto signUpDto) {
        User user = new User();
        user.setEmail(signUpDto.email());
        user.setPassword(new BCryptPasswordEncoder().encode(signUpDto.password()));
        user.setFirstName(signUpDto.firstName());
        user.setLastName(signUpDto.lastName());
        user.setRole(ROLE_ADMIN);
        return user;
    }

    private ResponseEntity<Map<String, Object>> handleUnauthorized() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(MESSAGE, "Invalid Credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
    }
}
