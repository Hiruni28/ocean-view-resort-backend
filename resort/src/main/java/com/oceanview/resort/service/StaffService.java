package com.oceanview.resort.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.oceanview.resort.model.Staff;
import com.oceanview.resort.repository.StaffRepository;

@Service
public class StaffService {

    private final StaffRepository repo;

    public StaffService(StaffRepository repo) {
        this.repo = repo;
    }

    public Staff login(String username, String password) {
        return repo.findByUsernameAndPassword(username, password);
    }

    public Staff save(Staff staff) {
        if (staff.getStatus() == null || staff.getStatus().isEmpty()) {
            staff.setStatus("ACTIVE");
        }
        return repo.save(staff);
    }

    public List<Staff> getAllStaff() {
        return repo.findAll();
    }

    public void deleteStaff(int staffId) {
        repo.deleteById(staffId);
    }


    public Staff getStaffById(int staffId) {
        return repo.findById(staffId).orElse(null);
    }
}
