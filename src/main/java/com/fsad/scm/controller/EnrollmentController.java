package com.fsad.scm.controller;

import com.fsad.scm.entity.Enrollment;
import com.fsad.scm.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("studentEmail") != null ? body.get("studentEmail") : body.get("email");
            String username = body.get("studentUsername") != null ? body.get("studentUsername") : body.get("username");
            String courseCode = body.get("courseCode");

            Enrollment enrollment = service.enroll(email, username, courseCode);

            if (enrollment == null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "waitlisted", true,
                    "message", "Course is full. You have been added to the waitlist."
                ));
            }
            return ResponseEntity.ok(Map.of(
                "success", true,
                "waitlisted", false,
                "enrolled", true,
                "enrollment", enrollment
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/unenroll")
    public ResponseEntity<?> unenroll(@RequestBody Map<String, String> body) {
        try {
            service.unenroll(
                body.get("studentEmail"),
                body.get("courseCode")
            );
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/enrollments/{email}")
    public List<Enrollment> getByEmail(@PathVariable String email) {
        return service.getStudentEnrollments(email);
    }
}
