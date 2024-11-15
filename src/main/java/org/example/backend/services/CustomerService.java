package org.example.backend.services;

import org.example.backend.db.entites.Customer;
import org.example.backend.dto.requests.UpdateProfileRequest;

public interface CustomerService {
    Customer updateProfile(Long customerId, UpdateProfileRequest request);
}
