package com.oceanview.resort.service;

import com.oceanview.resort.model.Customer;
import com.oceanview.resort.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    // ------ Register ------
    public Customer register(Customer c){
        return repo.save(c);
    }

    // ------ Login ------
    public Customer login(String email, String password){
        Customer c = repo.findByEmail(email);
        if(c != null && c.getPassword().equals(password)){
            return c;
        }
        return null;
    }

    // ------ Reset Password ------
    public boolean resetPassword(String email, String newPassword){
        Customer c = repo.findByEmail(email);

        if (c == null) return false;

        c.setPassword(newPassword);
        repo.save(c);

        return true;
    }

    // ------ Get All ------
    public List<Customer> getAllCustomers(){
        return repo.findAll();
    }

}