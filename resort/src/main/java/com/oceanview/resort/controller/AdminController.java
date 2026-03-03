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
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    // ---------- DTO (Inner Classes) ----------
    @Setter @Getter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Setter @Getter
    public static class ResetPasswordRequest {
        private String username;
        private String newPassword;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        boolean success = adminService.login(
                request.getUsername(),
                request.getPassword()
        );

        if (success) {
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("INVALID_CREDENTIALS");
        }
    }

    // ---------- RESET PASSWORD ----------
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        boolean updated = adminService.resetPassword(
                request.getUsername(),
                request.getNewPassword()
        );

        if (updated) {
            return ResponseEntity.ok("PASSWORD_UPDATED");
        } else {
            return ResponseEntity.status(404).body("USER_NOT_FOUND");
        }
    }

    // ---------- GET ALL ----------
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }
}