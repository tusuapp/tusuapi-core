package com.tusuapp.coreapi.models.dtos.notifications;

import com.tusuapp.coreapi.models.Notification;
import com.tusuapp.coreapi.utils.converters.TimeZoneConverter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.tusuapp.coreapi.utils.SessionUtil.getCurrentUserTimeZone;

/**
 * NotificationDto created by Rithik S(coderithik@gmail.com)
 **/
@Data
@Builder
public class NotificationDto {

    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String body;
    private String checkPoint;
    private String notes;
    private String medium;
    private Boolean readStatus;
    private String userCreatedAt;
    private String bookingNo;
    private Long createdBy;
    private Long updatedBy;
    private ZonedDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NotificationDto fromEntity(Notification entity) {
        if (entity == null) return null;

        return NotificationDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .title(entity.getTitle())
                .body(entity.getBody())
                .checkPoint(entity.getCheckPoint())
                .notes(entity.getNotes())
                .medium(entity.getMedium())
                .readStatus(entity.getReadStatus())
                .userCreatedAt(entity.getUserCreatedAt())
                .bookingNo(entity.getBookingNo())
                .createdBy(entity.getCreatedBy())
                .createdAt(TimeZoneConverter.getZonedUserTime(entity.getCreatedAt()))
                .updatedBy(entity.getUpdatedBy())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
