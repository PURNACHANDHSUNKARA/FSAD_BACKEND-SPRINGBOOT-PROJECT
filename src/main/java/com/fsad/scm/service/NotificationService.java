package com.fsad.scm.service;

import com.fsad.scm.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification create(Notification notification);

    List<Notification> getUserNotifications(String userId);

    void markRead(Long id);

    void clearAll(String userId);
}
