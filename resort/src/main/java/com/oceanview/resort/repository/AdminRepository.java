package com.oceanview.resort.repository;

import com.oceanview.resort.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // (Optional) Used only if you want JPA login
    Admin findByUsernameAndPassword(String username, String password);

    // For JPA lookup by username
    Admin findByUsername(String username);
}