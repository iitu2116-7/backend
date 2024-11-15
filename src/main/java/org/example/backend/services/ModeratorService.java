package org.example.backend.services;

import org.example.backend.db.entites.Moderator;
import org.example.backend.dto.requests.UpdateProfileRequest;

public interface ModeratorService {
    void toggleBlockedStatus(Long customerId);

    Moderator updateProfile(UpdateProfileRequest request);
}
