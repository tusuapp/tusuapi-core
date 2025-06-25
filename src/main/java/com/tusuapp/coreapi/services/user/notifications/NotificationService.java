package com.tusuapp.coreapi.services.user.notifications;


import com.tusuapp.coreapi.models.Notification;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.repositories.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    public static final String TYPE_PUSH_NOTIFICATION = "push-notification";

    @Autowired
    private NotificationRepo notificationRepo;

    public void addNotification(Long userId, String title, String body) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setType(TYPE_PUSH_NOTIFICATION);
        notification.setBody(body);
        notification.setUserCreatedAt(LocalDateTime.now().toString());
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepo.save(notification);
    }

    public void sendBookingNotifications(User student, User tutor) {
        addNotification(student.getId(),
                "Request sent for" + tutor.getFullName() + "'s class ",
                "Payment has been success and booking credits have been blocked");
        addNotification(tutor.getId(),
                "Booking request has been received from " + student.getFullName(),
                "You can accept or decline the booking");
    }

}
