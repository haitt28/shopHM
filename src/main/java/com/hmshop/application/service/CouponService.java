package com.hmshop.application.service;

import com.hmshop.application.entity.Coupon;
import com.hmshop.application.model.request.CreateCouponRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {

    Page<Coupon> adminGetListCoupon(String code, String name, String publish, String active, int page);

    Coupon createCoupon(CreateCouponRequest createCouponRequest);

    void updateCoupon(CreateCouponRequest createCouponRequest, long id);

    void deleteCoupon(long id);

    Coupon findCouponById(long id);

    //Kiểm tra có khuyến mại
    Coupon checkPublicCoupon();

    //Tính giá sản phẩm khi có khuyến mại
    BigDecimal calculateCouponPrice(BigDecimal price, Coupon coupon);

    //Lấy khuyến mại theo mã code
    Coupon checkCoupon(String code);

    //Lấy khuyến mại đang chạy và còn thời hạn
    List<Coupon> getAllValidCoupon();
}
