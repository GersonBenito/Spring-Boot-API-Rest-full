package com.application.rest.controllers.dto;

import com.application.rest.entities.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MakerDTO {
    private Long id;
    private String name;
    private List<Product> productList = new ArrayList<>();
}
