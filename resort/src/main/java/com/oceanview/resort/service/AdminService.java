package com.oceanview.resort.service;
import com.oceanview.resort.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean login(String username, String password) {
        try {
            String sql = "SELECT COUNT(*) FROM admin WHERE username = ? AND password = ?";

            Integer count = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{username, password},
                    Integer.class
            );

            System.out.println("DB COUNT = " + count);

            return count != null && count > 0;
        } catch (Exception e) {
            System.out.println("ERROR in AdminService.login(): " + e.getMessage());
            return false;
        }
    }
    // ~~~~~~~~~~~~~~~~~ GET ALL ADMINS ~~~~~~~~~~~~~~~~~
    public List<Admin> getAllAdmins() {

        String sql = "SELECT * FROM admin";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Admin admin = new Admin();
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setPassword(rs.getString("password"));
            return admin;
        });
    }
}
