package com.oceanview.resort.repository;

import com.oceanview.resort.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByUsernameAndPassword(String u,String p);
}
