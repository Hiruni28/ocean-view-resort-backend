package com.oceanview.resort.controller;

import com.oceanview.resort.model.Customer;
import com.oceanview.resort.service.CustomerService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    @Autowired
    private CustomerService service;

    // -------- REGISTER --------
    @PostMapping("/register")
    public Customer register(@RequestBody Customer c){
        return service.register(c);
    }

    // -------- LOGIN --------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer c){
        Customer logged = service.login(c.getEmail(), c.getPassword());
        if (logged != null) {
            return ResponseEntity.ok(logged);
        }
        return ResponseEntity.status(401).body("INVALID_CREDENTIALS");
    }

    // -------- RESET PASSWORD --------
    @Setter @Getter
    public static class ResetPasswordRequest {
        private String email;
        private String newPassword;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {

        boolean updated = service.resetPassword(req.getEmail(), req.getNewPassword());

        if (updated)
            return ResponseEntity.ok("PASSWORD_UPDATED");

        return ResponseEntity.status(404).body("EMAIL_NOT_FOUND");
    }

    // -------- GET ALL --------
    @GetMapping
    public List<Customer> getAllCustomers(){
        return service.getAllCustomers();
    }

}