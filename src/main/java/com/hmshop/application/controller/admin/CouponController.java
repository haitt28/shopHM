package com.hmshop.application.controller.admin;

import com.hmshop.application.entity.Coupon;
import com.hmshop.application.entity.Coupon;
import com.hmshop.application.model.request.CreateCouponRequest;
import com.hmshop.application.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/admin/coupons")
    public String getListCouponPages(Model model,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "", required = false) String code,
                                        @RequestParam(defaultValue = "", required = false) String name,
                                        @RequestParam(defaultValue = "", required = false) String publish,
                                        @RequestParam(defaultValue = "", required = false) String active) {


        Page<Coupon> CouponPage = couponService.adminGetListCoupon(code, name, publish, active, page);
        model.addAttribute("CouponPage", CouponPage);
        model.addAttribute("totalPages", CouponPage.getTotalPages());
        model.addAttribute("currentPage", CouponPage.getPageable().getPageNumber() + 1);
        return "admin/coupon/list";
    }

    @GetMapping("/admin/coupons/create")
    public String createCouponPage(Model model) {
        return "admin/Coupon/create";
    }

    @PostMapping("/api/admin/coupons")
    public ResponseEntity<Object> createCoupon(@Valid @RequestBody CreateCouponRequest createCouponRequest) {
        Coupon Coupon = couponService.createCoupon(createCouponRequest);
        return ResponseEntity.ok(Coupon.getId());
    }

    @GetMapping("/admin/coupons/update/{id}")
    public String updateCouponPage(Model model, @PathVariable long id) {
        Coupon Coupon = couponService.findCouponById(id);
        model.addAttribute("coupon", Coupon);
        return "admin/coupon/edit";
    }

    @PutMapping("/api/admin/coupons/{id}")
    public ResponseEntity<Object> updateCoupon(@Valid @RequestBody CreateCouponRequest createCouponRequest, @PathVariable long id) {
        couponService.updateCoupon(createCouponRequest, id);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/api/admin/coupons/{id}")
    public ResponseEntity<Object> deleteCoupon(@PathVariable long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Xóa khuyến mại thành công");
    }
}
