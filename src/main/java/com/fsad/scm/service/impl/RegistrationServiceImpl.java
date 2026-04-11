package com.fsad.scm.service.impl;

import com.fsad.scm.entity.Notification;
import com.fsad.scm.entity.RegistrationRequest;
import com.fsad.scm.repository.NotificationRepository;
import com.fsad.scm.repository.RegistrationRequestRepository;
import com.fsad.scm.repository.UserRepository;
import com.fsad.scm.service.RegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public RegistrationServiceImpl(RegistrationRequestRepository requestRepository,
                                   UserRepository userRepository,
                                   NotificationRepository notificationRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public RegistrationRequest request(String email, String username) {
        boolean pendingExists = requestRepository.findAll().stream()
                .anyMatch(r -> r.getEmail().equalsIgnoreCase(email)
                        && "PENDING".equals(r.getStatus()));
        if (pendingExists)
            throw new RuntimeException("A pending request already exists for this email");

        RegistrationRequest req = new RegistrationRequest();
        req.setEmail(email.trim().toLowerCase());
        req.setUsername(username);
        req.setStatus("PENDING");
        req.setRequestDate(LocalDateTime.now());
        return requestRepository.save(req);
    }

    @Override
    public List<RegistrationRequest> getAll() {
        return requestRepository.findAll();
    }

    @Override
    @Transactional
    public void approve(Long id) {
        RegistrationRequest req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        req.setStatus("APPROVED");
        req.setProcessedDate(LocalDateTime.now());
        requestRepository.save(req);

        userRepository.findByEmail(req.getEmail()).ifPresent(user -> {
            user.setRegistrationApproved(1);
            userRepository.save(user);
        });

        createNotification(req.getEmail(), "student",
                "Registration Approved",
                "Your registration has been approved. You can now enroll in courses.");
    }

    @Override
    @Transactional
    public void reject(Long id) {
        RegistrationRequest req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        req.setStatus("REJECTED");
        req.setProcessedDate(LocalDateTime.now());
        requestRepository.save(req);

        userRepository.findByEmail(req.getEmail()).ifPresent(user -> {
            user.setRegistrationApproved(0);
            userRepository.save(user);
        });

        createNotification(req.getEmail(), "student",
                "Registration Rejected",
                "Your registration request has been rejected. Please contact admin.");
    }

    private void createNotification(String userId, String userType, String title, String message) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setUserType(userType);
        n.setType("INFO");
        n.setTitle(title);
        n.setMessage(message);
        n.setTimestamp(LocalDateTime.now());
        n.setRead(false);
        notificationRepository.save(n);
    }
}
