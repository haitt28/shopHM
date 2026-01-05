package com.hmshop.application.controller.shop;

import com.hmshop.application.entity.*;
import com.hmshop.application.exception.BadRequestException;
import com.hmshop.application.exception.NotFoundException;
import com.hmshop.application.model.dto.CheckCoupon;
import com.hmshop.application.model.dto.DetailProductInfoDTO;
import com.hmshop.application.model.dto.PageableDTO;
import com.hmshop.application.model.dto.ProductInfoDTO;
import com.hmshop.application.model.request.CreateOrderRequest;
import com.hmshop.application.model.request.FilterProductRequest;
import com.hmshop.application.config.security.CustomUserDetails;
import com.hmshop.application.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hmshop.application.Constant.Constant.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final BrandService brandService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final CouponService couponService;

    @GetMapping
    public String homePage(Model model){

        //Lấy 5 sản phẩm mới nhất
        List<ProductInfoDTO> newProducts = productService.getListNewProducts();
        model.addAttribute("newProducts", newProducts);

        //Lấy 5 sản phẩm bán chạy nhất
        List<ProductInfoDTO> bestSellerProducts = productService.getListBestSellProducts();
        model.addAttribute("bestSellerProducts", bestSellerProducts);

        //Lấy 5 sản phẩm có lượt xem nhiều
        List<ProductInfoDTO> viewProducts = productService.getListViewProducts();
        model.addAttribute("viewProducts", viewProducts);

        //Lấy danh sách nhãn hiệu
        List<Brand> brands = brandService.getListBrand();
        model.addAttribute("brands",brands);

//        //Lấy 5 bài viết mới nhất
//        List<Post> posts = postService.getLatesPost();
//        model.addAttribute("posts", posts);

        return "shop/index";
    }

    @GetMapping("/{slug}/{id}")
    public String getProductDetail(Model model, @PathVariable String id){

        //Lấy thông tin sản phẩm
        DetailProductInfoDTO product;
        try {
            product = productService.getDetailProductById(id);
        } catch (NotFoundException ex) {
            return "error/404";
        } catch (Exception ex) {
            return "error/500";
        }
        model.addAttribute("product", product);

        //Lấy sản phẩm liên quan
        List<ProductInfoDTO> relatedProducts = productService.getRelatedProducts(id);
        model.addAttribute("relatedProducts", relatedProducts);

        //Lấy danh sách nhãn hiệu
        List<Brand> brands = brandService.getListBrand();
        model.addAttribute("brands",brands);

        // Lấy size có sẵn
        List<ProductVariant> productVariants = productService.getListSizeOfProduct(id);

        // ===== SAFE NULL / EMPTY =====
        if (productVariants == null || productVariants.isEmpty()) {
            model.addAttribute("availableSizes", List.of());
            model.addAttribute("availableColors", List.of());
            model.addAttribute("notFoundSize", true);
        }

        // ===== BUILD SIZE & COLOR LIST =====
        List<Integer> availableSizes = productVariants.stream()
                .map(ProductVariant::getSize)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        List<Integer> availableColors = productVariants.stream()
                .map(ProductVariant::getColor)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("availableColors", availableColors);
        if (!availableSizes.isEmpty()) {
            model.addAttribute("canBuy", true);
        } else {
            model.addAttribute("canBuy", false);
        }

        //Lấy danh sách size giầy
        model.addAttribute("sizes", PRODUCT_SIZE);
        model.addAttribute("colors", PRODUCT_COLOR);
        return "shop/detail";
    }

    @GetMapping("/dat-hang")
    public String getCartPage(Model model, @RequestParam String id,@RequestParam Integer size,@RequestParam Integer color){

        //Lấy chi tiết sản phẩm
        DetailProductInfoDTO product;
        try {
            product = productService.getDetailProductById(id);
        } catch (NotFoundException ex) {
            return "error/404";
        } catch (Exception ex) {
            return "error/500";
        }
        model.addAttribute("product", product);
        boolean notFoundSize = true;

        List<ProductVariant> productVariants = productService.getListSizeOfProduct(id);

        // ===== SAFE NULL / EMPTY =====
        if (productVariants == null || productVariants.isEmpty()) {
            model.addAttribute("availableSizes", List.of());
            model.addAttribute("availableColors", List.of());
            model.addAttribute("notFoundSize", true);
        }

        // ===== BUILD SIZE & COLOR LIST =====
        List<Integer> availableSizes = productVariants.stream()
                .map(ProductVariant::getSize)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        List<Integer> availableColors = productVariants.stream()
                .map(ProductVariant::getColor)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("availableColors", availableColors);

        // ===== CHECK NULL INPUT =====
        if (size != null && color != null) {
            notFoundSize = productVariants.stream()
                    .noneMatch(v ->
                            size.equals(v.getSize()) &&
                                    color.equals(v.getColor())
                    );
        }

        model.addAttribute("notFoundSize", notFoundSize);

        //Lấy danh sách size
        model.addAttribute("sizes", PRODUCT_SIZE);
        model.addAttribute("colors", PRODUCT_COLOR);
        model.addAttribute("size", size);
        model.addAttribute("color", color);

        return "shop/payment";
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Order order = orderService.createOrder(createOrderRequest, user.getId());

        return ResponseEntity.ok(order.getId());
    }

    @GetMapping("/products")
    public ResponseEntity<Object> getListBestSellProducts(){
        List<ProductInfoDTO> productInfoDTOS = productService.getListBestSellProducts();
        return ResponseEntity.ok(productInfoDTOS);
    }

    @GetMapping("/san-pham")
    public String getProductShopPages(Model model){

        //Lấy danh sách nhãn hiệu
        List<Brand> brands = brandService.getListBrand();
        model.addAttribute("brands",brands);
        List<Long> brandIds = new ArrayList<>();
        for (Brand brand : brands) {
            brandIds.add(brand.getId());
        }
        model.addAttribute("brandIds", brandIds);

        //Lấy danh sách danh mục
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories",categories);
        List<Long> categoryIds = new ArrayList<>();
        for (Category category : categories) {
            categoryIds.add(category.getId());
        }
        model.addAttribute("categoryIds", categoryIds);

        //Danh sách size của sản phẩm
        model.addAttribute("sizes", PRODUCT_SIZE);

        //Lấy danh sách sản phẩm
        FilterProductRequest req = new FilterProductRequest(brandIds, categoryIds, new ArrayList<>(), (long) 0, Long.MAX_VALUE, 1);
        PageableDTO result = productService.filterProduct(req);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("listProduct", result.getItems());

        return "shop/product";
    }

    @PostMapping("/api/san-pham/loc")
    public ResponseEntity<?> filterProduct(@RequestBody FilterProductRequest req) {
        // Validate
        if (req.getMinPrice() == null) {
            req.setMinPrice((long) 0);
        } else {
            if (req.getMinPrice() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mức giá phải lớn hơn 0");
            }
        }
        if (req.getMaxPrice() == null) {
            req.setMaxPrice(Long.MAX_VALUE);
        } else {
            if (req.getMaxPrice() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mức giá phải lớn hơn 0");
            }
        }

        PageableDTO result = productService.filterProduct(req);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/tim-kiem")
    public String searchProduct(Model model, @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer page) {

        PageableDTO result = productService.searchProductByKeyword(keyword, page);

        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("listProduct", result.getItems());
        model.addAttribute("keyword", keyword);
        if (((List<?>) result.getItems()).isEmpty()) {
            model.addAttribute("hasResult", false);
        } else {
            model.addAttribute("hasResult", true);
        }

        return "shop/search";
    }

    @GetMapping("/api/check-hidden-Coupon")
    public ResponseEntity<Object> checkCoupon(@RequestParam String code) {
        if (code == null || code == "") {
            throw new BadRequestException("Mã code trống");
        }

        Coupon Coupon = couponService.checkCoupon(code);
        if (Coupon == null) {
            throw new BadRequestException("Mã code không hợp lệ");
        }
        CheckCoupon checkCoupon = new CheckCoupon();
        checkCoupon.setDiscountType(Coupon.getDiscountType());
        checkCoupon.setDiscountValue(Coupon.getDiscountValue());
        checkCoupon.setMaximumDiscountValue(Coupon.getMaximumDiscountValue());
        return ResponseEntity.ok(checkCoupon);
    }

    @GetMapping("lien-he")
    public String contact(){
        return "shop/lien-he";
    }
    @GetMapping("huong-dan")
    public String buyGuide(){
        return "shop/buy-guide";
    }
    @GetMapping("doi-hang")
    public String doiHang(){
        return "shop/doi-hang";
    }

}
