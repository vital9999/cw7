package com.example.demo.repos;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, Integer> {
    List<Customer> findByUser(User user);
}
