package org.example.backend.services;

import org.example.backend.db.entites.Customer;
import org.example.backend.dto.dtos.CustomerDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;

public interface CustomerService {
    CustomerDTO getProfile(Long customerId);

    CustomerDTO updateProfile(Long customerId, UpdateProfileRequest request);
}
