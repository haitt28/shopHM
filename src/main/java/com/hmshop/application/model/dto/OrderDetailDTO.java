package com.hmshop.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private long id;

    private long totalPrice;

    private long productPrice;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private int status;

    private String statusText;

    private Integer size;

    private Integer color;

    private String productName;

    private String productImg;

    public OrderDetailDTO (long id, long totalPrice, long productPrice, String receiverName, String receiverPhone, String receiverAddress, int status, Integer size,Integer color, String productName, String productImg) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.productPrice = productPrice;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.status = status;
        this.size = size;
        this.color = color;
        this.productName = productName;
        this.productImg = productImg;
    }
}
