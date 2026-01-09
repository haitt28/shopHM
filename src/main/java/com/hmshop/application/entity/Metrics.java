package com.hmshop.application.entity;

import com.hmshop.application.model.dto.MetricsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "metricsDTO",
                        classes = @ConstructorResult(
                                targetClass = MetricsDTO.class,
                                columns = {
                                        @ColumnResult(name = "sales",type = Long.class),
                                        @ColumnResult(name = "profit",type = Long.class),
                                        @ColumnResult(name = "quantity",type = Integer.class),
                                        @ColumnResult(name = "createdAt",type = String.class)
                                }
                        )
                )
        }
)

@NamedNativeQuery(
        name = "getMetrics30Day",
        resultSetMapping = "metricsDTO",
        query = """
        SELECT
            DATE(s.created_at)        AS createdAt,
            SUM(s.sales)              AS sales,
            SUM(s.profit)             AS profit,
            SUM(s.quantity)           AS quantity
        FROM metrics s
        WHERE s.created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
        GROUP BY DATE(s.created_at)
        ORDER BY createdAt ASC
    """
)

@NamedNativeQuery(
        name = "getMetricsDayByDay",
        resultSetMapping = "metricsDTO",
        query = """
        SELECT
            DATE(s.created_at)  AS createdAt,
            SUM(s.sales)        AS sales,
            SUM(s.profit)       AS profit,
            SUM(s.quantity)     AS quantity
        FROM metrics s
        WHERE s.created_at >= ?1
          AND s.created_at <  DATE_ADD(?2, INTERVAL 1 DAY)
        GROUP BY DATE(s.created_at)
        ORDER BY createdAt ASC
    """
)

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "metrics")
public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "sales")
    private BigDecimal sales;
    @Column(name = "profit")
    private BigDecimal profit;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "created_at")
    private Timestamp createdAt;
}
