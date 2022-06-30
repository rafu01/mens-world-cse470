package com.mensworld.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mensworld.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
    @Override
    List<Product> findAll();
}

