package com.fsad.scm.controller;

import com.fsad.scm.entity.Notification;
import com.fsad.scm.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<Notification> get(@PathVariable String userId) {
        return service.getUserNotifications(userId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Notification notification) {
        return ResponseEntity.ok(service.create(notification));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        service.markRead(id);
        return ResponseEntity.ok(Map.of("message", "Marked as read"));
    }

    @PostMapping("/clear")
    public ResponseEntity<?> clear(@RequestBody Map<String, String> body) {
        service.clearAll(body.get("userId"));
        return ResponseEntity.ok(Map.of("message", "Cleared"));
    }
}
