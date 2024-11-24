package org.example.backend.controllers;

import lombok.AllArgsConstructor;
import org.example.backend.dto.dtos.CustomerDTO;
import org.example.backend.dto.dtos.NotificationDTO;
import org.example.backend.dto.dtos.TransactionDTO;
import org.example.backend.dto.requests.UpdateProfileRequest;
import org.example.backend.services.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    public ResponseEntity<Page<TransactionDTO>> getTransactionHistory(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo,
            @PageableDefault Pageable pageable) {
        Long customerId = Long.valueOf(data.get("id"));
        Page<TransactionDTO> transaction = customerService.getTransactionHistory(customerId, dateFrom, dateTo, pageable);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/get-notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        Long customerId = Long.valueOf(data.get("id"));
        List<NotificationDTO> notifications = customerService.getNotifications(customerId);
        return ResponseEntity.ok(notifications);
    }


}
