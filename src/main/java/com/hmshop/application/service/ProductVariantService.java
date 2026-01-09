package com.hmshop.application.service;

import com.hmshop.application.entity.ProductVariant;

import java.util.List;

public interface ProductVariantService {
    List<ProductVariant> getAllProductVariants();

    Long getTotalQuantity(String id);
}
