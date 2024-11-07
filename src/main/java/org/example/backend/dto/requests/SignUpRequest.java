package org.example.backend.dto.requests;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String verificationCode;
}
