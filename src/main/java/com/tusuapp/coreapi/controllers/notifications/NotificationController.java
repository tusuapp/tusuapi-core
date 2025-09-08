package com.tusuapp.coreapi.controllers.notifications;

import com.tusuapp.coreapi.services.user.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NotificationController created by Rithik S(coderithik@gmail.com)
 **/
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<?> getNotifications(){
        return notificationService.getUserNotifications();
    }


    @GetMapping("/poll")
    public ResponseEntity<?> getNotificationPoll(){
        return notificationService.pollNotification();
    }



}
