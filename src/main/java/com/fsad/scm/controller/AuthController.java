package com.fsad.scm.controller;

import com.fsad.scm.entity.User;
import com.fsad.scm.repository.UserRepository;
import com.fsad.scm.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
    "http://localhost:5173", "http://localhost:5174", "http://localhost:5175",
    "http://localhost:5176", "http://localhost:5177", "http://localhost:5178",
    "http://localhost:5179", "http://localhost:5180", "http://localhost:3000",
    "https://student-course-registration-o4aj.vercel.app"
}, allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists."));
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getRole() == null || newUser.getRole().isEmpty()) {
            newUser.setRole("student");
        }
        newUser.setRegistrationApproved(0);
        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Registration Successful. Awaiting admin approval."));
    }

    @PostMapping("/login/student")
    public ResponseEntity<?> loginStudent(@RequestBody Map<String, String> request) {
        return handleLogin(request.get("email"), request.get("password"), "student");
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> request) {
        return handleLogin(request.get("email"), request.get("password"), "admin");
    }

    private ResponseEntity<?> handleLogin(String email, String password, String targetRole) {
        Optional<User> userOpt = userRepository.findByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Account not found"));
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid Password"));
        }
        if (!user.getRole().equalsIgnoreCase(targetRole)) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of(
            "token", token,
            "id", user.getId(),
            "email", user.getEmail(),
            "username", user.getUsername() != null ? user.getUsername() : "",
            "role", user.getRole(),
            "registrationApproved", user.getRegistrationApproved() == null ? 0 : user.getRegistrationApproved()
        ));
    }
}
