package org.example.backend.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationDTO {
    private String message;
    private boolean isRead;
}
