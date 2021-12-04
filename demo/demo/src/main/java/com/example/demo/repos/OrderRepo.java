package com.example.demo.repos;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends CrudRepository<Order, Integer> {
    //List<Order> findAllByCustomer(Customer customer);
}
