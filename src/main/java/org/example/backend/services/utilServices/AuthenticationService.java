package org.example.backend.services.utilServices;


import lombok.RequiredArgsConstructor;

import org.example.backend.db.entites.Customer;
import org.example.backend.db.repositories.CustomerRepository;
import org.example.backend.dto.requests.SignInRequest;
import org.example.backend.dto.requests.SignUpRequest;
import org.example.backend.dto.responses.JwtAuthenticationResponse;
import org.example.backend.services.UserCreateService;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserCreateService userCreateService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


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
        try {
            String email = request.getUsername();
            Customer customer = customerRepository.findByUsername(email);
            if (customer != null && passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                var jwt = jwtService.generateToken(customer);
                return new JwtAuthenticationResponse(jwt, "success");
            }

            throw new BadCredentialsException("Неверные учетные данные");
        } catch (BadCredentialsException ex) {
            return new JwtAuthenticationResponse(null, "Неверные учетные данные");
        }
    }

}

