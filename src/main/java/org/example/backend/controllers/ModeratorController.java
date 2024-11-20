package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Moderator;
import org.example.backend.dto.dtos.CustomerDTO;
import org.example.backend.dto.dtos.ModeratorDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.example.backend.services.ModeratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ModeratorController extends BaseController {
    private ModeratorService moderatorService;

    @PutMapping("/{customerId}/toggle-block")
    public ResponseEntity<Void> toggleBlockStatus(@PathVariable Long customerId) {
        String role = data.get("role");
        if (role.equals("Admin")) {
            moderatorService.toggleBlockedStatus(customerId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/moderators/update-profile")
    public ResponseEntity<ModeratorDTO> updateModeratorProfile(
            @ModelAttribute UpdateProfileRequest request) {
        Long customerId = Long.valueOf(data.get("id"));
        ModeratorDTO updatedModerator = moderatorService.updateProfile(customerId, request);
        return ResponseEntity.ok(updatedModerator);
    }

    @GetMapping("/moderators/get-profile")
    public ResponseEntity<ModeratorDTO> getProfile() {
        Long moderatorId = Long.valueOf(data.get("id"));
        ModeratorDTO customer = moderatorService.getProfile(moderatorId);
        return ResponseEntity.ok(customer);
    }

}
