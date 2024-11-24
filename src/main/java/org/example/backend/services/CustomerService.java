package org.example.backend.services;

import org.example.backend.dto.dtos.CustomerDTO;
import org.example.backend.dto.dtos.NotificationDTO;
import org.example.backend.dto.dtos.TransactionDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomerService {
    CustomerDTO getProfile(Long customerId);

    CustomerDTO updateProfile(Long customerId, UpdateProfileRequest request);

    Page<TransactionDTO> getTransactionHistory(Long customerId, Date dateFrom, Date dateTo, Pageable pageable);

    List<NotificationDTO> getNotifications(Long customerId);
}
