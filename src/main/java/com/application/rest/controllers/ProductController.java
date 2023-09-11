package com.application.rest.controllers;

import com.application.rest.controllers.dto.ProductDTO;
import com.application.rest.entities.Product;
import com.application.rest.service.IProductService;
import com.application.rest.utils.RangePrice;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/api/product")
@Log
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if(productOptional.isPresent()){
            Product product = productOptional.get();

            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .maker(product.getMaker())
                    .build();

            response.put("message","Registro obtenido");
            response.put("Data", productDTO);

            return ResponseEntity.ok(response);
        }

        response.put("message", "Registro con id "+ id + " no encontrado");
        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(){

        Map<String, Object> response = new HashMap<>();

        List<ProductDTO> productList = productService.findAll()
                .stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .maker(product.getMaker())
                        .build()
                ).toList();

        response.put("message", "Registros obtenidos");
        response.put("Data", productList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/price")
    public ResponseEntity<?> findByPriceInRange(@RequestBody RangePrice rangePrice){

        Map<String, Object> response = new HashMap<>();

        if(rangePrice.minPrice == null || rangePrice.maxPrice == null){
            response.put("message", "Los datos estan vacios");
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        List<ProductDTO> productList = productService.findByPriceInRange(rangePrice.minPrice, rangePrice.maxPrice)
                .stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .maker(product.getMaker())
                        .build()
                ).toList();

        response.put("message", "Registros obtenidos");
        response.put("Data", productList);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        Map<String, Object> response = new HashMap<>();
        if(productDTO.getName().isBlank() || productDTO.getPrice() == null || productDTO.getMaker() == null){
            response.put("message", "Los datos estan vacios");
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        productService.save(
                Product.builder()
                        .name(productDTO.getName())
                        .price(productDTO.getPrice())
                        .maker(productDTO.getMaker())
                        .build()
        );

        response.put("message", "Registro creado");
        response.put("url", new URI("/api/product"));
        return new ResponseEntity<Object>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        Optional<Product> productOptional = productService.findById(id);
        Map<String, Object> response = new HashMap<>();

        if(productDTO.getName().isBlank() || productDTO.getPrice() == null || productDTO.getMaker() == null){
            response.put("message", "Los datos estan vacios");
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        if(productOptional.isPresent()){
            Product product = productOptional.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setMaker(productDTO.getMaker());

            productService.save(product);

            response.put("message", "Registro actualizado");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Registro con id "+ id +" no encontrado");
        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        if(id == null){
            response.put("message", "id null");
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<Product> productOptional = productService.findById(id);
        if(productOptional.isPresent()){
            productService.deleteById(id);
            response.put("message", "Registro eliminado");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Registro con id "+ id +" no encontrado");
        return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
    }
}
