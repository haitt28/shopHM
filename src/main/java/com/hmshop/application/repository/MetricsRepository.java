package com.hmshop.application.repository;

import com.hmshop.application.model.dto.MetricsDTO;
import com.hmshop.application.entity.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricsRepository extends JpaRepository<Metrics, Long> {

    @Query(name = "getMetrics30Day",nativeQuery = true)
    List<MetricsDTO> getMetrics30Day();

    @Query(name = "getMetricsDayByDay",nativeQuery = true)
    List<MetricsDTO> getMetricsDayByDay(String toDate, String formDate);

    @Query(value = "SELECT * FROM metrics  WHERE date_format(created_at,'%Y-%m-%d') = date_format(NOW(),'%Y-%m-%d')",nativeQuery = true)
    Metrics findByCreatedAT();

    @Query(value = "DELETE FROM metrics WHERE order_id = ?1", nativeQuery = true)
    void deleteByOrderId(Long orderId);

}
