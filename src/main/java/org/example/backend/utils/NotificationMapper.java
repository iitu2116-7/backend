package org.example.backend.utils;

import org.example.backend.db.entites.Notification;
import org.example.backend.dto.dtos.NotificationDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationMapper {

    public NotificationDTO toDto(Notification notification) {
        return new NotificationDTO(
                notification.getMessage(),
                notification.isRead()
        );
    }

    public List<NotificationDTO> toDto(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDto)
                .toList();
    }
}
