package com.fsad.scm.service.impl;

import com.fsad.scm.entity.Notification;
import com.fsad.scm.repository.NotificationRepository;
import com.fsad.scm.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification create(Notification notification) {
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        return repository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public void markRead(Long id) {
        repository.findById(id).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }

    @Override
    public void clearAll(String userId) {
        List<Notification> list = repository.findByUserId(userId);
        repository.deleteAll(list);
    }
}
