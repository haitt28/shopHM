package com.hmshop.application.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetricsDTO {
    private long sales;
    private long profit;
    private int quantity;
    private String createdAt;

    public MetricsDTO(long sales, long profit, int quantity, String createdAt){
        this.sales = sales;
        this.profit = profit;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }
}
