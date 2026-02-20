package com.oceanview.resort.controller;


import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.oceanview.resort.model.Staff;
import com.oceanview.resort.service.StaffService;

@CrossOrigin(origins = "http://localhost:5173") // allow frontend
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService service;

    public StaffController(StaffService service) {
        this.service = service;
    }

    // Staff login
    @PostMapping("/login")
    public Staff login(@RequestBody Staff staff) {
        return service.login(staff.getUsername(), staff.getPassword());
    }

    // Add new staff
    @PostMapping
    public Staff add(@RequestBody Staff staff) {
        return service.save(staff);
    }

    // Get all staff
    @GetMapping
    public List<Staff> getAll() {
        return service.getAllStaff();
    }

    // Update staff
    @PutMapping("/{id}")
    public Staff update(@PathVariable int id, @RequestBody Staff staff) {
        Staff existing = service.getStaffById(id);
        if (existing != null) {
            existing.setFullName(staff.getFullName());
            existing.setUsername(staff.getUsername());
            // Only update password if new password is provided
            if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
                existing.setPassword(staff.getPassword());
            }
            existing.setStatus(staff.getStatus());
            return service.save(existing);
        }
        return null;
    }


    // Delete staff
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.deleteStaff(id);
    }
}
