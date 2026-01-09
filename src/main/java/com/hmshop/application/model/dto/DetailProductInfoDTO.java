package com.hmshop.application.model.dto;

import com.hmshop.application.entity.Brand;
import com.hmshop.application.entity.Comment;
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
public class DetailProductInfoDTO {
    private String id;

    private String name;

    private String slug;

    private BigDecimal price;

    private int views;

    private long totalSold;

    private List<String> productImages;

    private List<String> feedbackImages;

    private BigDecimal CouponPrice;

    private String couponCode;

    private String description;

    private Brand brand;

    private List<Comment> comments;
}
