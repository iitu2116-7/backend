package org.example.backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Customer;
import org.example.backend.db.entites.VerificationCode;
import org.example.backend.db.enums.Role;
import org.example.backend.db.repositories.CustomerRepository;
import org.example.backend.db.repositories.VerificationCodeRepository;
import org.example.backend.dto.requests.SignUpRequest;
import org.example.backend.services.UserCreateService;
import org.example.backend.services.utilServices.EmailService;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserCreateServiceImpl implements UserCreateService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;


    @Override
    @Transactional
    public Pair<String, Long> create(SignUpRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            return Pair.of("Пользователь с таким почтовым адресом уже существует", 0L);
        }
        if (request.getVerificationCode().isEmpty()) {
            return handleSmsCodeGeneration(request);
        } else {
            handleCodeVerification(request);
            return handleCustomerCreate(request);
        }
    }

    private Pair<String, Long> handleSmsCodeGeneration(SignUpRequest request) {
        int random_number = generateRandomCode();
        saveVerificationCode(request.getEmail(), random_number);
        sendVerificationCode(request, random_number);
        return Pair.of("Код верификации отправлен", 0L);
    }

    private void handleCodeVerification(SignUpRequest request) {
        boolean codeExists = verificationCodeRepository.existsByEmailAndCode(request.getEmail(), request.getVerificationCode());

        if (codeExists) {
            Pair.of("Код верификации верный", 0L);

            verificationCodeRepository.deleteByEmailAndCode(request.getEmail(), request.getVerificationCode());
        } else {
            Pair.of("Код верификации неверный", 0L);
        }
    }

    @Override
    public Pair<String,Long> passwordRecovery(SignUpRequest request) {
        if(request.getVerificationCode().isEmpty()){
            return handleVerificationCodeGenerationPasswordRecovery(request);
        } else if (!request.getPassword().isEmpty()){
            return handleVerificationForPassRecovery(request);
        } else  {
            handleCodeVerification(request);
            return Pair.of("Код верификации верный", 0L);
        }
    }

    private Pair<String, Long> handleVerificationCodeGenerationPasswordRecovery(SignUpRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail());
        if (customer == null){
            return Pair.of("Клиент отсутствует",0L);
        }
        int random_number = generateRandomCode();
        saveVerificationCode(request.getEmail(), random_number);
        sendVerificationCode(request, random_number);
        return Pair.of("Код верификации отправлен", 0L);
    }

    private Pair<String, Long> handleVerificationForPassRecovery(SignUpRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail());
        if (customer == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }

        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customerRepository.save(customer);

        return Pair.of("Пароль успешно изменен", 0L);
    }

    private Pair<String,Long> handleCustomerCreate(SignUpRequest request){
        Customer customer = createCustomer(request);
        Customer customerSave = customerRepository.save(customer);
        return Pair.of("Пользователь создан",customerSave.getId());
    }

    private void sendVerificationCode(SignUpRequest request, int random_number) {
        String subject = "Добро пожаловать в Smart Money!";
        String message = String.format(
                "<html>" +
                        "<body>" +
                        "<img src='resources/static/logoSmartMoney.png' alt='Smart Money Logo' style='width:150px;height:auto;'/><br/><br/>" +
                        "Здравствуйте, %s!<br/><br/>" +
                        "Мы рады приветствовать вас в Smart Money. Чтобы завершить регистрацию и подтвердить вашу почту, " +
                        "введите, пожалуйста, следующий код верификации. Если вы не запрашивали регистрацию на нашем сайте, " +
                        "просто проигнорируйте это письмо.<br/><br/>" +
                        "<strong>Код верификации: %s</strong><br/><br/>" +
                        "Если у вас возникли вопросы, пожалуйста, свяжитесь с нашей поддержкой, и мы с радостью вам поможем." +
                        "</body>" +
                        "</html>",
                request.getFirstName(),
                random_number
        );
        emailService.sendEmail(request.getEmail(), subject, message);
    }

    private int generateRandomCode() {
        return 111111 + (int) (Math.random() * ((999999 - 111111) + 1));
    }

    private void saveVerificationCode(String username, int code) {
        VerificationCode verificationCode = VerificationCode.builder()
                .code(code)
                .email(username)
                .build();
        verificationCodeRepository.save(verificationCode);
    }

    private Customer createCustomer(SignUpRequest request) {
        return Customer.builder()
                .role(Role.RoleClient)
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdDate(new Date())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public Customer getByUsername(String username) {
        return customerRepository.findByEmailOptional(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

}
