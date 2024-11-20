package org.example.backend.services;

import org.example.backend.dto.dtos.ModeratorDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;

public interface ModeratorService {
    void toggleBlockedStatus(Long customerId);

    ModeratorDTO updateProfile(Long customerId, UpdateProfileRequest request);

    ModeratorDTO getProfile(Long customerId);
}
