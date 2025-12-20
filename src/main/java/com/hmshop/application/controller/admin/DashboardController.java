package com.hmshop.application.controller.admin;

import com.hmshop.application.model.dto.ChartDTO;
import com.hmshop.application.model.dto.MetricsDTO;
import com.hmshop.application.model.request.FilterDayByDay;
import com.hmshop.application.repository.*;
import com.hmshop.application.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MetricsRepository metricsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String dashboard(Model model){
        return "admin/index";
    }

//    @GetMapping("/api/admin/count/posts")
//    public ResponseEntity<Object> getCountPost(){
//        long countPosts = postService.getCountPost();
//        return ResponseEntity.ok(countPosts);
//    }

    @GetMapping("/api/admin/count/products")
    public ResponseEntity<Object> getCountProduct(){
        long countProducts = productService.getCountProduct();
        return ResponseEntity.ok(countProducts);
    }

    @GetMapping("/api/admin/count/orders")
    public ResponseEntity<Object> getCountOrders(){
        long countOrders = orderService.getCountOrder();
        return ResponseEntity.ok(countOrders);
    }

    @GetMapping("/api/admin/count/categories")
    public ResponseEntity<Object> getCountCategories(){
        long countCategories = categoryService.getCountCategories();
        return ResponseEntity.ok(countCategories);
    }

    @GetMapping("/api/admin/count/brands")
    public ResponseEntity<Object> getCountBrands(){
        long countBrands = brandService.getCountBrands();
        return ResponseEntity.ok(countBrands);
    }

    @GetMapping("/api/admin/count/users")
    public ResponseEntity<Object> getCountUsers(){
        long countUsers = userRepository.count();
        return ResponseEntity.ok(countUsers);
    }

    @GetMapping("/api/admin/metrics")
    public ResponseEntity<Object> getMetrics30Day(){
        List<MetricsDTO> metrics = metricsRepository.getMetrics30Day();
        return ResponseEntity.ok(metrics);
    }

    @PostMapping("/api/admin/metrics")
    public ResponseEntity<Object> getMetricsDayByDay(@RequestBody FilterDayByDay filterDayByDay){
        List<MetricsDTO> metricsDTOS = metricsRepository.getMetricsDayByDay(filterDayByDay.getToDate(),filterDayByDay.getFromDate());
        return ResponseEntity.ok(metricsDTOS);
    }

    @GetMapping("/api/admin/product-order-categories")
    public ResponseEntity<Object> getListProductOrderCategories(){
        List<ChartDTO> chartDTOS = categoryRepository.getListProductOrderCategories();
        return ResponseEntity.ok(chartDTOS);
    }

    @GetMapping("/api/admin/product-order-brands")
    public ResponseEntity<Object> getProductOrderBrands(){
        List<ChartDTO> chartDTOS = brandRepository.getProductOrderBrands();
        return ResponseEntity.ok(chartDTOS);
    }

    @GetMapping("/api/admin/product-order")
    public ResponseEntity<Object> getProductOrder(){
        Pageable pageable = PageRequest.of(0,10);
        Date date = new Date();
        List<ChartDTO> chartDTOS = productRepository.getProductOrders(pageable, date.getMonth() +1, date.getYear() + 1900);
        return ResponseEntity.ok(chartDTOS);
    }
}
