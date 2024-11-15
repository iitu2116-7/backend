package org.example.backend.dto.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private Long id;
    private String email;
    private MultipartFile profilePicture;
}

