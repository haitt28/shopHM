package com.hmshop.application.service.impl;

import com.hmshop.application.entity.ProductVariant;
import com.hmshop.application.repository.ProductVariantRepository;
import com.hmshop.application.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Override
    public List<ProductVariant> getAllProductVariants() {
        return productVariantRepository.getAllProductVariants();
    }

    @Override
    public Long getTotalQuantity(String id) {
        return productVariantRepository.sumQuantityProduct(id);
    }
}
