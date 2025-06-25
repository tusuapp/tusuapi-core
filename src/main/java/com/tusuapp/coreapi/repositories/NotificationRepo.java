package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification,Long> {
}
