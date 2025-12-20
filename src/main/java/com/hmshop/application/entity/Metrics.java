package com.hmshop.application.entity;

import com.hmshop.application.model.dto.MetricsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
        query = "SELECT s.sales, s.profit, s.quantity, date_format(s.created_at,'%Y-%m-%d') as createdAt FROM Metrics s WHERE date_format(s.created_at,'%Y-%m-%d') BETWEEN DATE_SUB(NOW(), INTERVAL 30 DAY) AND NOW() ORDER BY createdAt ASC "
)
@NamedNativeQuery(
        name = "getMetricsDayByDay",
        resultSetMapping = "metricsDTO",
        query = "SELECT s.sales, s.profit, s.quantity, date_format(s.created_at,'%Y-%m-%d') as createdAt FROM Metrics s WHERE date_format(s.created_at,'%Y-%m-%d') >=?1 AND date_format(s.created_at,'%Y-%m-%d') <=?2 ORDER BY createdAt ASC "
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
    private long sales;
    @Column(name = "profit")
    private long profit;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @Column(name = "created_at")
    private Timestamp createdAt;
}
