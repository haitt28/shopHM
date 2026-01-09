package com.hmshop.application.service;

import com.hmshop.application.entity.Order;

public interface EmailService {
    void sendOrderConfirmation(Order event);
    void sendOrderSuccess(Order event);
}
