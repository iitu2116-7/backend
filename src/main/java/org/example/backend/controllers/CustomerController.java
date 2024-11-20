package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.db.entites.Notification;
import org.example.backend.dto.dtos.CustomerDTO;
import org.example.backend.dto.dtos.TransactionDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.example.backend.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController extends BaseController {
    private CustomerService customerService;

    @PutMapping("/update-profile")
    public ResponseEntity<CustomerDTO> updateCustomerProfile(
            @ModelAttribute UpdateProfileRequest request) {
        Long customerId = Long.valueOf(data.get("id"));
        CustomerDTO updatedCustomer = customerService.updateProfile(customerId, request);
        return ResponseEntity.ok(updatedCustomer);
    }


    @GetMapping("/get-profile")
    public ResponseEntity<CustomerDTO> getProfile() {
        Long customerId = Long.valueOf(data.get("id"));
        CustomerDTO customer = customerService.getProfile(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/get-history")
    public ResponseEntity<TransactionDTO> getTransactionHistory() {
        Long customerId = Long.valueOf(data.get("id"));
        TransactionDTO transaction = customerService.getTransactionHistory(customerId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/get-notification")
    public ResponseEntity<Notification> getNotifications() {
        Long customerId = Long.valueOf(data.get("id"));
        Notification notification = customerService.getNotifications(customerId);
        return ResponseEntity.ok(notification);
    }


}
