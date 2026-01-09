package com.hmshop.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
    private String id;
    private String name;
    private  String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private long totalSold;
    private int status;
    private List<String> images;
    private List<String> feedBackImages;
    private List<String> color;
}
