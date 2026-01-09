package com.hmshop.application.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderInfoDTO {
    private long id;

    private String productId;

    private String code;

    private long totalPrice;

    private Integer size;

    private Long colorId;

    private String productName;

    private String productImg;

    private String sizeText;

    private String colorText;

    public OrderInfoDTO(long id, String productId, String code, long totalPrice, Integer size, Long colorId, String productName, String productImg) {
        this.id = id;
        this.productId = productId;
        this.code = code;
        this.totalPrice = totalPrice;
        this.size = size;
        this.colorId = colorId;
        this.productName = productName;
        this.productImg = productImg;
    }
}