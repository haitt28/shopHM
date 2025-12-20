package com.hmshop.application.repository;

import com.hmshop.application.entity.Coupon;
import com.hmshop.application.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM Coupon p " +
            "WHERE p.coupon_code LIKE CONCAT('%',?1,'%') " +
            "AND p.name LIKE CONCAT('%',?2,'%') " +
            "AND p.is_public LIKE CONCAT('%',?3,'%') " +
            "AND p.is_active LIKE CONCAT('%',?4,'%')")
    Page<Coupon> adminGetListCoupon(String code, String name, String publish, String active, Pageable pageable);

    //Kiểm tra có khuyến mại đang chạy
    @Query(nativeQuery = true, value = "SELECT * FROM Coupon p WHERE p.is_active = true AND p.is_public = true AND expired_at > now()")
    Coupon checkHasPublicCoupon();

    //Lấy khuyến mại theo mã code
    Optional<Coupon> findByCouponCode(String code);

    //Lấy khuyến mại đang chạy theo mã code
    @Query(nativeQuery = true,value = "SELECT  * FROM Coupon p WHERE p.is_active = true AND p.coupon_code = ?1")
    Coupon checkCoupon(String code);

    //Lấy khuyến mại đang chạy và còn thời hạn
    @Query(nativeQuery = true,value = "SELECT * FROM  Coupon p WHERE p.expired_at > now() AND p.is_active = true")
    List<Coupon> getAllValidCoupon();

}
