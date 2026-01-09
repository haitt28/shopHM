package com.hmshop.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false,length = 300)
    private String name;
    @Column(name = "coupon_code",unique = true)
    private String couponCode;
    @Column(name = "discount_type")
    private int discountType;
    @Column(name = "discount_value")
    private long discountValue;
    @Column(name = "maximum_discount_value")
    private long maximumDiscountValue;
    @Column(name = "is_active",columnDefinition = "TINYINT(1)")
    private boolean isActive;
    @Column(name = "is_public",columnDefinition = "TINYINT(1)")
    private boolean isPublic;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "expired_at")
    private Timestamp expiredAt;

    public Coupon(Order.UsedCoupon Coupon) {
        this.couponCode = Coupon.getCouponCode();
        this.discountType = Coupon.getDiscountType();
        this.discountValue = Coupon.getDiscountValue();
        this.maximumDiscountValue = Coupon.getMaximumDiscountValue();
    }
}