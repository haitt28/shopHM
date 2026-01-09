package com.hmshop.application.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailDTO {

    private Long id;
    private String productId;
    private String code;
    private Long totalPrice;
    private Long productPrice;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer status;
    private Integer size;
    private Long colorId;
    private String productName;
    private String productImg;
    private String statusText;

    public OrderDetailDTO(
            Long id,
            String productId,
            String code,
            Long totalPrice,
            Long productPrice,
            String receiverName,
            String receiverPhone,
            String receiverAddress,
            Integer status,
            Integer size,
            Long colorId,
            String productName,
            String productImg
    ) {
        this.id = id;
        this.productId = productId;
        this.code = code;
        this.totalPrice = totalPrice;
        this.productPrice = productPrice;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.status = status;
        this.size = size;
        this.colorId = colorId;
        this.productName = productName;
        this.productImg = productImg;
    }
}
