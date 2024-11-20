package org.example.backend.utils;

import org.example.backend.db.entites.Customer;
import org.example.backend.dto.dtos.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerDTO toDto(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getPreferredCurrency(),
                customer.getPhotoUrl()
        );
    }
}
