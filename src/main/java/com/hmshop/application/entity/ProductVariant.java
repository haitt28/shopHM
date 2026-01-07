package com.hmshop.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(
        name = "product_variant",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "size", "color_id"})
        }
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "size", nullable = false)
    private Integer size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @Column(name = "quantity")
    private Integer quantity;

}



