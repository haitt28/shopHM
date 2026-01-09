package com.hmshop.application.repository;

import com.hmshop.application.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    //Lấy size của sản phẩm
    @Query(nativeQuery = true,value = "SELECT ps.size FROM product_variant ps WHERE  product_id = ?1 AND ps.quantity > 0")
    List<Integer> findAllSizeOfProduct(String id);

    List<ProductVariant> findByProductId(String id);
    @Query(value = "SELECT * FROM product_variant", nativeQuery = true)
    List<ProductVariant> getAllProductVariants();

    //Kiểm trả size sản phẩm
    @Query(value = "SELECT * FROM product_variant WHERE product_id = ?1 AND size = ?2 AND quantity >0", nativeQuery = true)
    ProductVariant checkProductAndSizeAvailable(String id, int size);

    //Kiểm trả size sản phẩm
    @Query(value = "SELECT * FROM product_variant WHERE product_id = ?1 AND size = ?2 AND color_id = ?3 AND quantity >0", nativeQuery = true)
    ProductVariant checkProductAndSizeAvailableV2(String id, Integer size,Long color);

    //Trừ 1 sản phẩm theo size
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product_variant set quantity = quantity - 1 where product_id = ?1 and size = ?2")
    void minusOneProductBySize(String id, int size);

    //Cộng 1 sản phẩm theo size
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product_variant set quantity = quantity + 1 where product_id = ?1 and size = ?2")
    void plusOneProductBySize(String id, int size);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Delete from product_variant where product_id = ?1")
    void deleteByProductId(String id);

    @Query(value = "SELECT COALESCE(SUM(quantity), 0) FROM product_variant WHERE product_id = ?1", nativeQuery = true)
    long sumQuantityProduct(String productId);
}
