package com.tusuapp.coreapi.repositories;

import com.tusuapp.coreapi.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification,Long> {
    Page<Notification> findAllByUserId(Long currentUserId, Pageable pageable);

    Page<Notification> findAllByUserIdAndReadStatus(Long currentUserId, boolean status, Pageable pageable);

    Page<Notification> findAllByUserIdAndShownStatus(Long currentUserId, boolean status, Pageable pageable);
}
