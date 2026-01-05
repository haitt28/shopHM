package com.hmshop.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderInfoDTO {
    private long id;

    private long totalPrice;

    private Integer size;

    private Integer color;

    private String productName;

    private String productImg;

    public OrderInfoDTO(long id, long totalPrice, Integer size,Integer color, String productName, String productImg) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.size = size;
        this.color = color;
        this.productName = productName;
        this.productImg = productImg;
    }
}