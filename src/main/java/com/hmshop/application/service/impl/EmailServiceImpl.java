package com.hmshop.application.service.impl;

import com.hmshop.application.entity.Order;
import com.hmshop.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private String buildEmailContent(Order order) {
        return """
            <h3>Cảm ơn bạn đã mua hàng!</h3>
            <p>Sản phẩm: %s</p>
            <p>Size: %s</p>
            <p>Màu sắc: %s</p>
            <p>Số lượng: %d</p>
            <p>Giá: %s VND</p>
            <p><b>Tổng tiền: %s VND</b></p>
            """
                .formatted(
                        order.getProduct().getName(),
                        order.getSize(),
                        order.getColor().getName(),
                        order.getQuantity(),
                        formatMoney(order.getPrice()),
                        formatMoney(order.getTotalPrice())
                );
    }

    private String formatMoney(BigDecimal amount) {
        return NumberFormat
                .getInstance(new Locale("vi", "VN"))
                .format(amount);
    }

    @Override
    public void sendOrderConfirmation(Order event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.getReceiverEmail());
            helper.setSubject("Xác nhận đơn hàng : " + event.getCode());
            helper.setText(buildEmailContent(event), true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendOrderSuccess(Order event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(event.getReceiverEmail());
            helper.setSubject("Đơn hàng đã giao : " + event.getId() + " thành công");
            helper.setText(buildEmailContent(event), true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
