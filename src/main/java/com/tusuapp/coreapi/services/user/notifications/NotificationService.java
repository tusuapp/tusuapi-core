package com.tusuapp.coreapi.services.user.notifications;


import com.tusuapp.coreapi.models.Notification;
import com.tusuapp.coreapi.models.User;
import com.tusuapp.coreapi.models.dtos.notifications.NotificationDto;
import com.tusuapp.coreapi.repositories.NotificationRepo;
import com.tusuapp.coreapi.utils.converters.TimeZoneConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserId;

@Service
public class NotificationService {

    public static final String TYPE_PUSH_NOTIFICATION = "push-notification";

    @Autowired
    private NotificationRepo notificationRepo;

    public void addNotification(Long userId, String title, String body, boolean shownStatus) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setType(TYPE_PUSH_NOTIFICATION);
        notification.setBody(body);
        notification.setUserCreatedAt(TimeZoneConverter.getCurrentUTCTime().toString());
        notification.setCreatedAt(TimeZoneConverter.getCurrentUTCTime());
        notificationRepo.save(notification);
    }

    public void sendBookingNotifications(User student, User tutor) {
        addNotification(student.getId(),
                "Request sent for" + tutor.getFullName() + "'s class ",
                "Payment has been success and booking credits have been blocked", true);
        addNotification(tutor.getId(),
                "Booking request has been received from " + student.getFullName(),
                "You can accept or decline the booking", false);
    }

    public void sendRejectNotifications(User student, User tutor) {
        addNotification(student.getId(),
                "Class from " + tutor.getFullName() + " has been rejected",
                "Credits have been refunded to your account", false);
        addNotification(tutor.getId(),
                "Booking from " + student.getFullName() + " has been rejected",
                "You can accept or decline the booking", true);
    }

    public void sendRescheduleNotification(User student, User tutor) {
        addNotification(student.getId(),
                "Class from " + tutor.getFullName() + " has been rescheduled by tutor",
                "You can accept or decline the request", false);
        addNotification(tutor.getId(),
                "Sent reschedule request to " + student.getFullName(),
                "You can wait for their approval", true);
    }
    public ResponseEntity<?> getUserNotifications() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationRepo.findAllByUserIdAndReadStatus(getCurrentUserId(), false, pageable);
        List<NotificationDto> dtos = notifications.getContent().stream().map(NotificationDto::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> pollNotification() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationRepo.findAllByUserIdAndShownStatus(getCurrentUserId(), false, pageable);
        if (notifications.getContent().isEmpty()) {
            return ResponseEntity.ok().build();
        }
        List<Notification> updated = notifications.getContent().stream().peek((item) -> item.setShownStatus(true)).toList();
        notificationRepo.saveAll(updated);
        JSONObject response = new JSONObject();
        response.put("count", updated.size());
        return ResponseEntity.ok(response.toMap());
    }


}

