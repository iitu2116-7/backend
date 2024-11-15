package org.example.backend.services.utilServices;


import lombok.RequiredArgsConstructor;

import org.example.backend.db.entites.Customer;
import org.example.backend.db.entites.Moderator;
import org.example.backend.db.repositories.CustomerRepository;
import org.example.backend.db.repositories.ModeratorRepository;
import org.example.backend.dto.requests.SignInRequest;
import org.example.backend.dto.requests.SignUpRequest;
import org.example.backend.dto.responses.JwtAuthenticationResponse;
import org.example.backend.services.UserCreateService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserCreateService userCreateService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModeratorRepository moderatorRepository;


    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        try {
            Pair<String, Long> result = userCreateService.create(request);
            if (result.getFirst().equals("Пользователь создан")){

                var customer = customerRepository.getReferenceById(result.getSecond());
                var jwt = jwtService.generateToken(customer);
                return new JwtAuthenticationResponse(jwt, result.getFirst());
            }else {
                return new JwtAuthenticationResponse(null,result.getFirst());
            }
        }catch (BadCredentialsException ex) {
            return new JwtAuthenticationResponse(null, "Неверно учетные данные");
        }
    }

    /**
     * Аутентификация
     *
     * @param request данные
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Customer customer = customerRepository.findByEmail(email);
        if (isValidCustomer(customer, password)) {
            var jwt = jwtService.generateToken(customer);
            return new JwtAuthenticationResponse(jwt, "success");
        }

        Moderator moderator = moderatorRepository.findByEmail(email);
        if (isValidModerator(moderator, password)) {
            var jwt = jwtService.generateTokenModerator(moderator);
            return new JwtAuthenticationResponse(jwt, "success");
        }

        return new JwtAuthenticationResponse(null, "Неверные учетные данные");
    }

    private boolean isValidCustomer(Customer customer, String password) {
        if (customer == null) {
            return false;
        }
        if (customer.isBlocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is blocked");
        }
        return passwordEncoder.matches(password, customer.getPassword());
    }

    private boolean isValidModerator(Moderator moderator, String password) {
        return moderator != null && passwordEncoder.matches(password, moderator.getPassword());
    }


}

