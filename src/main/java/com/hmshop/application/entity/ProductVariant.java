package com.hmshop.application.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "product_variant",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_variant",
                        columnNames = {"product_id", "size", "color"}
                )
        }
)
public class ProductVariant {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "size", nullable = false)
    private int size;

    @Column(name = "color", nullable = false)
    private int color;

    @Column(name = "quantity")
    private int quantity;

    @PrePersist
    void generateId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductVariant)) return false;
        ProductVariant variant = (ProductVariant) o;
        return id != null && id.equals(variant.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


