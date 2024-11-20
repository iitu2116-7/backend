package org.example.backend.services.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Customer;
import org.example.backend.db.entites.Notification;
import org.example.backend.db.enums.Currency;
import org.example.backend.db.repositories.CustomerRepository;
import org.example.backend.db.repositories.NotificationRepository;
import org.example.backend.db.repositories.TransactionRepository;
import org.example.backend.dto.dtos.CustomerDTO;
import org.example.backend.dto.dtos.TransactionDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.example.backend.dto.responses.MinioConstants;
import org.example.backend.services.CustomerService;
import org.example.backend.services.utilServices.EntityUpdateUtil;
import org.example.backend.utils.CustomerMapper;
import org.example.backend.utils.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final MinioClient minioClient;
    private final TransactionRepository transactionRepository;


    private static final String BUCKET_NAME = "profile-photo";
    private final TransactionMapper transactionMapper;
    private final NotificationRepository notificationRepository;

    @Override
    public CustomerDTO getProfile(Long customerId) {
        Objects.requireNonNull(customerId, "ID cannot be null");

        return customerRepository.findById(customerId)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("NOT FOUND"));
    }

    @Override
    public CustomerDTO updateProfile(Long customerId, UpdateProfileRequest request) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        EntityUpdateUtil.updateField(existingCustomer::setEmail, request.getEmail());
        EntityUpdateUtil.updateField(existingCustomer::setFirstname, request.getFirstname());
        EntityUpdateUtil.updateField(existingCustomer::setLastname, request.getLastname());

        Optional.ofNullable(request.getPreferredCurrency())
                .map(String::toUpperCase)
                .ifPresent(currency -> {
                    try {
                        existingCustomer.setPreferredCurrency(Currency.valueOf(currency));
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid preferred currency value");
                    }
                });

        Optional.ofNullable(request.getProfilePicture())
                .filter(file -> !file.isEmpty())
                .map(this::uploadPhotoToMinio)
                .ifPresent(existingCustomer::setPhotoUrl);

        existingCustomer.setUpdatedDate(new Date());
        customerRepository.save(existingCustomer);

        return customerMapper.toDto(existingCustomer);
    }


    public String uploadPhotoToMinio(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return MinioConstants.BASE_URL_PROFILE_PHOTO + fileName;
        } catch (IOException | MinioException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Ошибка при загрузке файла: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<TransactionDTO> getTransactionHistory(Long customerId, Date dateFrom, Date dateTo, Pageable pageable) {

        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        Objects.requireNonNull(dateFrom, "Date from cannot be null");
        Objects.requireNonNull(dateTo, "Date to cannot be null");
        if (dateFrom.after(dateTo)) {
            throw new IllegalArgumentException("Date from cannot be after date to");
        }

        return transactionRepository.findByCustomerIdAndDateRange(customerId, dateFrom, dateTo, pageable).map(transactionMapper::toDto);

    }

    @Override
    public List<Notification> getNotifications(Long customerId) {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        return notificationRepository.findByCustomerId(customerId);
    }


}
