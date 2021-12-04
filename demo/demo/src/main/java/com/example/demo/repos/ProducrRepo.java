package com.example.demo.repos;

import com.example.demo.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducrRepo extends CrudRepository<Product, Integer> {
}
