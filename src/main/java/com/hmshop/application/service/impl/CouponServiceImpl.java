package com.hmshop.application.service.impl;

import com.hmshop.application.entity.Coupon;
import com.hmshop.application.exception.BadRequestException;
import com.hmshop.application.exception.InternalServerException;
import com.hmshop.application.exception.NotFoundException;
import com.hmshop.application.repository.CouponRepository;
import com.hmshop.application.entity.Coupon;
import com.hmshop.application.model.request.CreateCouponRequest;
import com.hmshop.application.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.hmshop.application.Constant.Constant.DISCOUNT_PERCENT;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public Page<Coupon> adminGetListCoupon(String code, String name, String publish, String active, int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        return couponRepository.adminGetListCoupon(code, name, publish, active, pageable);
    }

    @Override
    public Coupon createCoupon(CreateCouponRequest createCouponRequest) {
        //Check mã đã tồn tại chưa
        Optional<Coupon> rs = couponRepository.findByCouponCode(createCouponRequest.getCode());
        if (rs.isPresent()) {
            throw new BadRequestException("Mã khuyến mại đã tồn tại!");
        }

        //Check validate
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (createCouponRequest.getExpiredDate().before(now)) {
            throw new BadRequestException("Hạn khuyến mại không hợp lệ");
        }
        if (createCouponRequest.getDiscountType() == DISCOUNT_PERCENT) {
            if (createCouponRequest.getDiscountValue() < 1 || createCouponRequest.getDiscountValue() > 100) {
                throw new BadRequestException("Mức giảm giá từ 1% - 100%");
            }
            if (createCouponRequest.getMaxValue() < 1000) {
                throw new BadRequestException("Mức giảm giá tối đa phải lớn hơn 1000");
            }
        } else {
            if (createCouponRequest.getDiscountValue() < 1000) {
                throw new BadRequestException("Mức giảm giá phải lớn hơn 1000");
            }
        }

        //Check có khuyến mại nào đang chạy hay chưa

        if (createCouponRequest.isPublic() && createCouponRequest.isActive()) {
            Coupon alreadyCoupon = couponRepository.checkHasPublicCouponV2();
            if (alreadyCoupon != null) {
                throw new BadRequestException("Chương trình khuyến mãi công khai \"" + alreadyCoupon.getCouponCode() + "\" đang chạy. Không thể tạo mới");
            }
        }

        Coupon Coupon = new Coupon();
        Coupon.setCouponCode(createCouponRequest.getCode());
        Coupon.setName(createCouponRequest.getName());
        Coupon.setActive(createCouponRequest.isActive());
        Coupon.setPublic(createCouponRequest.isPublic());
        Coupon.setExpiredAt(createCouponRequest.getExpiredDate());
        Coupon.setDiscountType(createCouponRequest.getDiscountType());
        Coupon.setDiscountValue(createCouponRequest.getDiscountValue());
        if (createCouponRequest.getDiscountType() == DISCOUNT_PERCENT) {
            Coupon.setMaximumDiscountValue(createCouponRequest.getMaxValue());
        } else {
            Coupon.setMaximumDiscountValue(createCouponRequest.getDiscountValue());
        }
        Coupon.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        couponRepository.save(Coupon);
        return Coupon;
    }

    @Override
    public void updateCoupon(CreateCouponRequest createCouponRequest, long id) {
        Optional<Coupon> rs = couponRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("Khuyến mại không tồn tại");
        }

        //check validate
        if (createCouponRequest.getDiscountType() == DISCOUNT_PERCENT) {
            if (createCouponRequest.getDiscountValue() < 1 || createCouponRequest.getDiscountValue() > 100) {
                throw new BadRequestException("Mức giảm giá từ 1 - 100%");
            }
            if (createCouponRequest.getMaxValue() < 1000) {
                throw new BadRequestException("Mức giảm giá tối đa phải lớn hơn 1000");
            }
        } else {
            if (createCouponRequest.getDiscountValue() < 1000) {
                throw new BadRequestException("Mức giảm giá phải lớn hơn 1000");
            }
        }

        //Check có khuyến mại nào đang chạy hay không
        if (createCouponRequest.isActive() && createCouponRequest.isPublic()) {
            Coupon alreadyCoupon = couponRepository.checkHasPublicCouponV2();
            if (alreadyCoupon != null) {
                throw new BadRequestException("Chương trình khuyến mãi công khai \"" + alreadyCoupon.getCouponCode() + "\" đang chạy. Không thể tạo mới");
            }
        }

        //Kiểm tra mã code
        rs = couponRepository.findByCouponCode(createCouponRequest.getCode());
        if (rs.isPresent() && rs.get().getId() != id) {
            throw new BadRequestException("Mã code đã tồn tại trong hệ thống");
        }

        Coupon Coupon = new Coupon();
        Coupon.setCouponCode(createCouponRequest.getCode());
        Coupon.setName(createCouponRequest.getName());
        Coupon.setExpiredAt(createCouponRequest.getExpiredDate());
        Coupon.setDiscountType(createCouponRequest.getDiscountType());
        Coupon.setDiscountValue(createCouponRequest.getDiscountValue());
        if (createCouponRequest.getDiscountType() == DISCOUNT_PERCENT) {
            Coupon.setMaximumDiscountValue(createCouponRequest.getMaxValue());
        } else {
            Coupon.setDiscountValue(createCouponRequest.getDiscountValue());
        }
        Coupon.setActive(createCouponRequest.isActive());
        Coupon.setPublic(createCouponRequest.isPublic());
        Coupon.setCreatedAt(rs.get().getCreatedAt());
        Coupon.setId(id);

        try {
            couponRepository.save(Coupon);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi cập nhật khuyến mãi");
        }
    }

    @Override
    public void deleteCoupon(long id) {
        Optional<Coupon> Coupon = couponRepository.findById(id);
        if (Coupon.isEmpty()) {
            throw new BadRequestException("Khuyến mại không tồn tại");
        }

        //Check Coupon used
//        int check = CouponRepository.checkCouponUsed(Coupon.get().getCouponCode());
//        if (check > 0) {
//            throw new BadRequestException("Khuyến mại đã được sử dụng không thể xóa");
//        }

        try {
            couponRepository.deleteById(id);
        }catch (Exception e){
            throw new InternalServerException("Lỗi khi xóa khuyến mại");
        }
    }

    @Override
    public Coupon findCouponById(long id) {
        Optional<Coupon> Coupon = couponRepository.findById(id);
        if (Coupon.isEmpty()) {
            throw new NotFoundException("Khuyến mại không tồn tại");
        }
        return Coupon.get();
    }

    @Override
    public Coupon checkPublicCoupon() {
        return couponRepository.checkHasPublicCoupon();
    }

    @Override
    public BigDecimal calculateCouponPrice(BigDecimal price, Coupon coupon) {

        if (price == null || coupon == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal maxDiscount = BigDecimal.valueOf(coupon.getMaximumDiscountValue());

        if (coupon.getDiscountType() == DISCOUNT_PERCENT) {
            discountAmount = price
                    .multiply(BigDecimal.valueOf(coupon.getDiscountValue()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discountAmount = BigDecimal.valueOf(coupon.getDiscountValue());
        }

        // apply max discount
        if (discountAmount.compareTo(maxDiscount) > 0) {
            discountAmount = maxDiscount;
        }

        BigDecimal finalPrice = price.subtract(discountAmount);

        // price không âm
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }

        return finalPrice;
    }

    @Override
    public Coupon checkCoupon(String code) {
        return couponRepository.checkCoupon(code);
    }

    @Override
    public List<Coupon> getAllValidCoupon() {
        return couponRepository.getAllValidCoupon();
    }
}
