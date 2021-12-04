package com.example.demo.repos;

import com.example.demo.model.OrderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepo extends CrudRepository<OrderDetails, Integer> {

}
