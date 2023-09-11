package com.application.rest.repository;

import com.application.rest.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    // using Query methods
    List<Product> findProductByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // using query and JPQL
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2") // SELECT * FROM product WHERE price BETWEEN 1500 AND 2500
    List<Product> findProductByPriceInRange(BigDecimal minPrice, BigDecimal maxDPrice);
}
