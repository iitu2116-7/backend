package org.example.backend.dto.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String email;
    private String firstname;
    private String lastname;
    private String preferredCurrency;
    private MultipartFile profilePicture;
}

