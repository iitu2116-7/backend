package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Customer;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.example.backend.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController extends BaseController {
    private CustomerService customerService;

    @PutMapping("/update-profile")
    public ResponseEntity<Customer> updateCustomerProfile(
            @ModelAttribute UpdateProfileRequest request) {
        Long customerId = Long.valueOf(data.get("id"));
        Customer updatedCustomer = customerService.updateProfile(customerId, request);
        return ResponseEntity.ok(updatedCustomer);
    }
}
