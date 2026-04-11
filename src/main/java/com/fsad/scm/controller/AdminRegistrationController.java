package com.fsad.scm.controller;

import com.fsad.scm.entity.User;
import com.fsad.scm.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/registrations")
@CrossOrigin(origins = {
    "http://localhost:5173", "http://localhost:5174", "http://localhost:5175",
    "http://localhost:5176", "http://localhost:5177", "http://localhost:5178",
    "http://localhost:5179", "http://localhost:5180", "http://localhost:3000",
    "https://student-course-registration-o4aj.vercel.app"
}, allowCredentials = "true")
public class AdminRegistrationController {

    private final UserRepository userRepository;

    public AdminRegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getPending() {
        List<User> pending = userRepository.findAll().stream()
                .filter(u -> "student".equals(u.getRole()))
                .filter(u -> u.getRegistrationApproved() == null || u.getRegistrationApproved() == 0)
                .toList();
        return ResponseEntity.ok(pending);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            user.setRegistrationApproved(1);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Approved"));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Rejected and removed"));
    }
}
