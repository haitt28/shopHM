package com.hmshop.application.MailListener;

import com.hmshop.application.entity.Order;
import com.hmshop.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMailListener {

    private final EmailService mailService;

    @Async("mailExecutor")
    @EventListener
    public void handle(Order event) {
        mailService.sendOrderConfirmation(event);
    }
}