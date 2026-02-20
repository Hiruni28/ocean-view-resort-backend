package com.oceanview.resort.controller;

import com.oceanview.resort.model.Admin;
import com.oceanview.resort.service.AdminService;
import com.oceanview.resort.util.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Setter
    @Getter
    public static class LoginRequest {
        private String username;
        private String password;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        boolean success = adminService.login(
                request.getUsername(),
                request.getPassword()
        );

        if (success) {
            String token = jwtUtil.generateToken(request.getUsername());

            System.out.println("JWT TOKEN = " + token); //  PRINT

            return ResponseEntity.ok(token);
        }
        else {
            return ResponseEntity.status(401).body("INVALID_CREDENTIALS");
        }
    }
    @GetMapping
    public List<Admin> getAllAdmins(){
        return adminService.getAllAdmins();
    }

}

