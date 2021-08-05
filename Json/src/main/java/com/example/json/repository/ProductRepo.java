package com.example.json.repository;

import com.example.json.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findProductById(Long id);
    Set<Product> findProductBySellerNotNull();

    @Query(value = "SELECT p FROM Product p WHERE p.buyer is null")
    Set<Product> findAllNotBoughtProducts();
}
