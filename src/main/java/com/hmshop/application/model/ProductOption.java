package com.hmshop.application.model;

import lombok.Getter;

@Getter
public class ProductOption {
    private final int id;
    private final String value;

    public ProductOption(int id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ProductOption{id=" + id + ", value='" + value + "'}";
    }
}

