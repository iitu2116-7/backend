package org.example.backend.utils;

import org.example.backend.db.entites.Moderator;
import org.example.backend.dto.dtos.ModeratorDTO;
import org.springframework.stereotype.Component;

@Component
public class ModeratorMapper {

    public ModeratorDTO toDto(Moderator moderator) {
        return new ModeratorDTO (
                moderator.getId(),
                moderator.getEmail(),
                moderator.getPhotoUrl()
        );
    }
}
