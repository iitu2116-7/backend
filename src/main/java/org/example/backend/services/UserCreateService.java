package org.example.backend.services;


import org.example.backend.dto.requests.SignUpRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserCreateService extends UserDetailsService {

    Pair<String,Long> passwordRecovery(SignUpRequest request) throws Exception;

    UserDetailsService userDetailsService();

    Pair<String,Long> create(SignUpRequest request);
}
