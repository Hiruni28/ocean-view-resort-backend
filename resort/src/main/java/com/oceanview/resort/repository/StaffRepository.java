package com.oceanview.resort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oceanview.resort.model.Staff;


public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByUsernameAndPassword(String username, String password);
}
