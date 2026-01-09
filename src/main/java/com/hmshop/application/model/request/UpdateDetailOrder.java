package com.hmshop.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateDetailOrder {
    @NotBlank(message = "Sản phẩm trống")
    @JsonProperty("product_id")
    private String productId;

    private int size;

    @JsonProperty("coupon_code")
    private String couponCode;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("product_price")
    private BigDecimal productPrice;
}
