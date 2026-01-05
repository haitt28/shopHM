package com.hmshop.application.controller.admin;

import com.hmshop.application.config.security.CustomUserDetails;
import com.hmshop.application.entity.Coupon;
import com.hmshop.application.entity.Order;
import com.hmshop.application.entity.User;
import com.hmshop.application.exception.BadRequestException;
import com.hmshop.application.model.dto.OrderDetailDTO;
import com.hmshop.application.model.dto.OrderInfoDTO;
import com.hmshop.application.model.dto.ShortProductInfoDTO;
import com.hmshop.application.model.request.CreateOrderRequest;
import com.hmshop.application.model.request.UpdateDetailOrder;
import com.hmshop.application.model.request.UpdateStatusOrderRequest;
import com.hmshop.application.service.CouponService;
import com.hmshop.application.service.OrderService;
import com.hmshop.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hmshop.application.Constant.Constant.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final CouponService couponService;

    @GetMapping("/admin/orders")
    public String getListOrderPage(Model model,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "", required = false) String id,
                                   @RequestParam(defaultValue = "", required = false) String name,
                                   @RequestParam(defaultValue = "", required = false) String phone,
                                   @RequestParam(defaultValue = "", required = false) String status,
                                   @RequestParam(defaultValue = "", required = false) String product) {

        //Lấy danh sách sản phẩm
        List<ShortProductInfoDTO> productList = productService.getListProduct();
        model.addAttribute("productList", productList);


        //Lấy danh sách đơn hàng
        Page<Order> orderPage = orderService.adminGetListOrders(id, name, phone, status, product, page);
        model.addAttribute("orderPage", orderPage.getContent());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", orderPage.getPageable().getPageNumber() + 1);

        return "admin/order/list";
    }

    @GetMapping("/admin/orders/create")
    public String createOrderPage(Model model) {

        //Get list product
        List<ShortProductInfoDTO> products = productService.getAvailableProducts();
        model.addAttribute("products", products);

        // Get list size
        model.addAttribute("sizeVn", PRODUCT_SIZE);

//        //Get list valid Coupon
        List<Coupon> coupons = couponService.getAllValidCoupon();
        model.addAttribute("Coupons", coupons);
        return "admin/order/create";
    }

    @PostMapping("/api/admin/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Order order = orderService.createOrder(createOrderRequest, user.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/admin/orders/update/{id}")
    public String updateOrderPage(Model model, @PathVariable long id) {

        Order order = orderService.findOrderById(id);
        model.addAttribute("order", order);

        if (order.getStatus() == ORDER_STATUS) {
            // Get list product to select
            List<ShortProductInfoDTO> products = productService.getAvailableProducts();
            model.addAttribute("products", products);

            // Get list valid Coupon
            List<Coupon> Coupons = couponService.getAllValidCoupon();
            model.addAttribute("Coupons", Coupons);
            if (order.getCoupon() != null) {
                boolean validCoupon = false;
                for (Coupon Coupon : Coupons) {
                    if (Coupon.getCouponCode().equals(order.getCoupon().getCouponCode())) {
                        validCoupon = true;
                        break;
                    }
                }
                if (!validCoupon) {
                    Coupons.add(new Coupon(order.getCoupon()));
                }
            }

            // Check size available
            boolean sizeIsAvailable = productService.checkProductSizeAvailable(order.getProduct().getId(), order.getSize(),order.getColor());
            model.addAttribute("sizeIsAvailable", sizeIsAvailable);
        }
        model.addAttribute("sizeMap", PRODUCT_SIZE_MAP);
        model.addAttribute("colorMap", PRODUCT_COLOR_MAP);

        return "admin/order/edit";
    }

    @PutMapping("/api/admin/orders/update-detail/{id}")
    public ResponseEntity<Object> updateDetailOrder(@Valid @RequestBody UpdateDetailOrder detailOrder, @PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        orderService.updateDetailOrder(detailOrder, id, user.getId());
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @PutMapping("/api/admin/orders/update-status/{id}")
    public ResponseEntity<Object> updateStatusOrder(@Valid @RequestBody UpdateStatusOrderRequest updateStatusOrderRequest, @PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        orderService.updateStatusOrder(updateStatusOrderRequest, id, user.getId());
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @GetMapping("/tai-khoan/lich-su-giao-dich")
    public String getOrderHistoryPage(Model model) {

        //Get list order pending
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<OrderInfoDTO> orderList = orderService.getListOrderOfPersonByStatus(ORDER_STATUS, user.getId());
        model.addAttribute("orderList", orderList);
        model.addAttribute("sizeMap", PRODUCT_SIZE_MAP);
        model.addAttribute("colorMap", PRODUCT_COLOR_MAP);
        return "shop/order_history";
    }

    @GetMapping("/api/get-order-list")
    public ResponseEntity<Object> getListOrderByStatus(@RequestParam int status) {
        // Validate status
        if (!LIST_ORDER_STATUS.contains(status)) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
        }

        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<OrderInfoDTO> orders = orderService.getListOrderOfPersonByStatus(status, user.getId());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/tai-khoan/lich-su-giao-dich/{id}")
    public String getDetailOrderPage(Model model, @PathVariable int id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        OrderDetailDTO order = orderService.userGetDetailById(id, user.getId());
        if (order == null) {
            return "error/404";
        }
        model.addAttribute("order", order);
        model.addAttribute("sizeMap", PRODUCT_SIZE_MAP);
        model.addAttribute("colorMap", PRODUCT_COLOR_MAP);
        if (order.getStatus() == ORDER_STATUS) {
            model.addAttribute("canCancel", true);
        } else {
            model.addAttribute("canCancel", false);
        }

        return "shop/order-detail";
    }

    @PostMapping("/api/cancel-order/{id}")
    public ResponseEntity<Object> cancelOrder(@PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        orderService.userCancelOrder(id, user.getId());

        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }

}
