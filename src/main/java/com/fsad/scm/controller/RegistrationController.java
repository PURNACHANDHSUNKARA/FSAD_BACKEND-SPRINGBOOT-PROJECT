package com.fsad.scm.controller;

import com.fsad.scm.entity.RegistrationRequest;
import com.fsad.scm.service.RegistrationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registration")
@CrossOrigin(origins = "*")
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public RegistrationRequest register(@RequestBody Map<String, String> body) {
        return service.request(body.get("email"), body.get("username"));
    }

    @GetMapping
    public List<RegistrationRequest> getAll() {
        return service.getAll();
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity.ok("Approved");
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        service.reject(id);
        return ResponseEntity.ok("Rejected");
    }
}
