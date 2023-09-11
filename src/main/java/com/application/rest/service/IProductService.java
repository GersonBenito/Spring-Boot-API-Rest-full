package com.application.rest.service;

import com.application.rest.entities.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    public List<Product> findAll();

    public List<Product> findByPriceInRange(BigDecimal minPrice, BigDecimal maxDecimal);

    public Optional<Product> findById(Long id);

    public void save(Product product);

    public void deleteById(Long id);
}
