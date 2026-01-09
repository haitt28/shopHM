package com.hmshop.application.service.impl;

import com.hmshop.application.entity.*;
import com.hmshop.application.exception.BadRequestException;
import com.hmshop.application.exception.InternalServerException;
import com.hmshop.application.exception.NotFoundException;
import com.hmshop.application.model.dto.OrderDetailDTO;
import com.hmshop.application.model.dto.OrderInfoDTO;
import com.hmshop.application.model.request.CreateOrderRequest;
import com.hmshop.application.model.request.UpdateDetailOrder;
import com.hmshop.application.model.request.UpdateStatusOrderRequest;
import com.hmshop.application.repository.*;
import com.hmshop.application.service.CouponService;
import com.hmshop.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hmshop.application.Constant.Constant.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final CouponService couponService;
    private final MetricsRepository metricsRepository;
    private final ColorRepository colorRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<Order> adminGetListOrders(String id, String name, String phone, String status, String product, int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        return orderRepository.adminGetListOrder(id, name, phone, status, product, pageable);
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest, long userId) {

        //Kiểm tra sản phẩm có tồn tại
        Optional<Product> product = productRepository.findById(createOrderRequest.getProductId());
        if (product.isEmpty()) {
            throw new NotFoundException("Sản phẩm không tồn tại!");
        }
        Integer size = createOrderRequest.getSize();
        Long colorId = createOrderRequest.getColor();
        Color color = new Color();
        if (colorId != null && colorId > 0) {
            color = colorRepository.getReferenceById(colorId);
        }

        //Kiểm tra size có sẵn
        ProductVariant productSize = productVariantRepository.checkProductAndSizeAvailableV2(createOrderRequest.getProductId(), size,color.getId());
        if (ObjectUtils.isEmpty(productSize)) {
            throw new BadRequestException("Size giày sản phẩm tạm hết, Vui lòng chọn sản phẩm khác!");
        }

        //Kiểm tra giá sản phẩm
        if (product.get().getSalePrice()
                .compareTo(createOrderRequest.getProductPrice()) != 0) {
            throw new BadRequestException("Giá sản phẩm thay đổi, Vui lòng đặt hàng lại!");
        }
        Order order = new Order();
        User user = new User();
        String code = "HM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        user.setId(userId);
        order.setCreatedBy(user);
        order.setBuyer(user);
        order.setCode(code);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setReceiverAddress(createOrderRequest.getReceiverAddress());
        order.setReceiverName(createOrderRequest.getReceiverName());
        order.setReceiverPhone(createOrderRequest.getReceiverPhone());
        order.setReceiverEmail(createOrderRequest.getReceiverEmail());
        order.setNote(createOrderRequest.getNote());
        order.setSize(createOrderRequest.getSize());
        order.setColor(color);
        order.setPrice(createOrderRequest.getProductPrice());
        order.setTotalPrice(createOrderRequest.getTotalPrice());
        order.setStatus(ORDER_STATUS);
        order.setQuantity(1);
        order.setProduct(product.get());

        orderRepository.save(order);
        eventPublisher.publishEvent(order);
        return order;

    }

    @Override
    @Transactional
    public void updateDetailOrder(UpdateDetailOrder updateDetailOrder, long id, long userId) {
        //Kiểm trả có đơn hàng
        Optional<Order> rs = orderRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }

        Order order = rs.get();
        //Kiểm tra trạng thái đơn hàng
        if (order.getStatus() != ORDER_STATUS) {
            throw new BadRequestException("Chỉ cập nhật đơn hàng ở trạng thái chờ lấy hàng");
        }

        //Kiểm tra size sản phẩm
        Optional<Product> product = productRepository.findById(updateDetailOrder.getProductId());
        if (product.isEmpty()) {
            throw new BadRequestException("Sản phẩm không tồn tại");
        }
        //Kiểm tra giá
        if (product.get().getSalePrice() != updateDetailOrder.getProductPrice()) {
            throw new BadRequestException("Giá sản phẩm thay đổi vui lòng đặt hàng lại");
        }

        ProductVariant productSize = productVariantRepository.checkProductAndSizeAvailable(updateDetailOrder.getProductId(), updateDetailOrder.getSize());
        if (productSize == null) {
            throw new BadRequestException("Size giày sản phẩm tạm hết, Vui lòng chọn sản phẩm khác");
        }

        //Kiểm tra khuyến mại
        if (updateDetailOrder.getCouponCode() != "") {
            Coupon Coupon = couponService.checkCoupon(updateDetailOrder.getCouponCode());
            if (Coupon == null) {
                throw new NotFoundException("Mã khuyến mãi không tồn tại hoặc chưa được kích hoạt");
            }
            BigDecimal CouponPrice = couponService.calculateCouponPrice(updateDetailOrder.getProductPrice(), Coupon);
            if (CouponPrice != updateDetailOrder.getTotalPrice()) {
                throw new BadRequestException("Tổng giá trị đơn hàng thay đổi. Vui lòng kiểm tra và đặt lại đơn hàng");
            }
            Order.UsedCoupon usedCoupon = new Order.UsedCoupon(updateDetailOrder.getCouponCode(), Coupon.getDiscountType(), Coupon.getDiscountValue(), Coupon.getMaximumDiscountValue());
            order.setCoupon(usedCoupon);
        }

        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        order.setProduct(product.get());
        order.setSize(updateDetailOrder.getSize());
        order.setPrice(updateDetailOrder.getProductPrice());
        order.setTotalPrice(updateDetailOrder.getTotalPrice());


        order.setStatus(ORDER_STATUS);
        User user = new User();
        user.setId(userId);
        order.setModifiedBy(user);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi cập nhật");
        }
    }


    @Override
    public Order findOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }
        return order.get();
    }

    @Override
    @Transactional
    public void updateStatusOrder(UpdateStatusOrderRequest req, long orderId, long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Đơn hàng không tồn tại"));

        int currentStatus = order.getStatus();
        int newStatus = req.getStatus();

        // ===== 1. Validate status =====
        if (!LIST_ORDER_STATUS.contains(newStatus)) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
        }

        // ===== 2. XỬ LÝ THEO TRẠNG THÁI HIỆN TẠI =====
        switch (currentStatus) {

            case ORDER_STATUS:
                handleFromOrderStatus(order, req);
                break;

            case DELIVERY_STATUS:
                handleFromDeliveryStatus(order, newStatus);
                break;

            case COMPLETED_STATUS:
                handleFromCompletedStatus(order, newStatus);
                break;

            default:
                if (currentStatus != newStatus) {
                    throw new BadRequestException("Không thể chuyển trạng thái");
                }
        }

        // ===== 3. UPDATE COMMON INFO =====
        User user = new User();
        user.setId(userId);

        order.setStatus(newStatus);
        order.setNote(req.getNote());
        order.setModifiedBy(user);
        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        orderRepository.save(order);
    }

    @Override
    public List<OrderInfoDTO> getListOrderOfPersonByStatus(int status, long userId) {
        return orderRepository.getListOrderOfPersonByStatus(status, userId);
    }

    @Override
    public OrderDetailDTO userGetDetailById(long id, long userId) {
        OrderDetailDTO order = orderRepository.userGetDetailById(id, userId);
        if (order == null) {
            return null;
        }

        if (order.getStatus() == ORDER_STATUS) {
            order.setStatusText("Chờ lấy hàng");
        } else if (order.getStatus() == DELIVERY_STATUS) {
            order.setStatusText("Đang giao hàng");
        } else if (order.getStatus() == COMPLETED_STATUS) {
            order.setStatusText("Đã giao hàng");
        } else if (order.getStatus() == CANCELED_STATUS) {
            order.setStatusText("Đơn hàng đã trả lại");
        } else if (order.getStatus() == RETURNED_STATUS) {
            order.setStatusText("Đơn hàng đã hủy");
        }
        return order;
    }

    @Override
    public void userCancelOrder(long id, long userId) {
        Optional<Order> rs = orderRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("Đơn hàng không tồn tại");
        }
        Order order = rs.get();
        if (order.getBuyer().getId() != userId) {
            throw new BadRequestException("Bạn không phải chủ nhân đơn hàng");
        }
        if (order.getStatus() != ORDER_STATUS) {
            throw new BadRequestException("Trạng thái đơn hàng không phù hợp để hủy. Vui lòng liên hệ với shop để được hỗ trợ");
        }

        order.setStatus(CANCELED_STATUS);
        orderRepository.save(order);
    }

    @Override
    public long getCountOrder() {
        return orderRepository.count();
    }

    /**
     * Tạo metric cho 1 đơn hàng
     * KHÔNG cộng dồn
     * KHÔNG lưu giá vốn
     */
    public void createMetric(Order order) {

        // ===== 1. DOANH THU =====
        // Tiền khách thực trả (đã trừ khuyến mãi)
        BigDecimal sales = order.getTotalPrice();

        // ===== 2. SỐ LƯỢNG =====
        int quantity = order.getQuantity();

        // ===== 3. GIÁ NHẬP (CHỈ DÙNG ĐỂ TÍNH PROFIT) =====
        BigDecimal importPrice = order.getProduct().getPrice();

        // ===== 4. TÍNH PROFIT =====
        // profit = sales - (giá nhập * số lượng)
        BigDecimal profit = sales.subtract(
                importPrice.multiply(BigDecimal.valueOf(quantity))
        );

        // ===== 5. TẠO METRIC =====
        Metrics metric = new Metrics();
        metric.setOrder(order);
        metric.setSales(sales);
        metric.setQuantity(quantity);
        metric.setProfit(profit);
        metric.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // ===== 6. LƯU DB =====
        metricsRepository.save(metric);
    }

    private void handleFromOrderStatus(Order order, UpdateStatusOrderRequest req) {

        if (req.getStatus() == ORDER_STATUS) {
            order.setReceiverName(req.getReceiverName());
            order.setReceiverPhone(req.getReceiverPhone());
            order.setReceiverAddress(req.getReceiverAddress());
            return;
        }

        if (req.getStatus() == DELIVERY_STATUS) {
            productVariantRepository.minusOneProductBySize(
                    order.getProduct().getId(), order.getSize()
            );
            return;
        }

        throw new BadRequestException("Không thể chuyển trạng thái");
    }

    private void handleFromDeliveryStatus(Order order, int newStatus) {

        if (newStatus == COMPLETED_STATUS) {

            productRepository.plusOneProductTotalSold(order.getProduct().getId());

            // ✅ CHỈ TẠO METRIC 1 LẦN DUY NHẤT
            createMetric(order);
            return;
        }

        if (newStatus == CANCELED_STATUS || newStatus == RETURNED_STATUS) {
            productVariantRepository.plusOneProductBySize(
                    order.getProduct().getId(), order.getSize()
            );
            return;
        }

        throw new BadRequestException("Không thể chuyển trạng thái");
    }

    private void handleFromCompletedStatus(Order order, int newStatus) {

        if (newStatus == RETURNED_STATUS) {

            productVariantRepository.plusOneProductBySize(
                    order.getProduct().getId(), order.getSize()
            );

            productRepository.minusOneProductTotalSold(order.getProduct().getId());

            // 👉 XÓA metric hoặc đánh dấu invalid
            metricsRepository.deleteByOrderId(order.getId());

            return;
        }

        throw new BadRequestException("Không thể chuyển trạng thái");
    }
}
