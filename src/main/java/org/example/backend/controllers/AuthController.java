package org.example.backend.controllers;

import org.springframework.data.util.Pair;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.requests.SignInRequest;
import org.example.backend.dto.requests.SignUpRequest;
import org.example.backend.dto.responses.JwtAuthenticationResponse;
import org.example.backend.services.UserCreateService;
import org.example.backend.services.utilServices.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserCreateService userCreateService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request){
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request){
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Забыли пароль")
    @PostMapping("/forgot-password")
    public ResponseEntity<Pair<String, Long>> forgotYourPassword(@RequestBody @Valid SignUpRequest request) throws Exception {
        return ResponseEntity.ok(userCreateService.passwordRecovery(request));
    }
}
