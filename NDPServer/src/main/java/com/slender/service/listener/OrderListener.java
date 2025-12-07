package com.slender.service.listener;

import com.slender.config.manager.JsonParserManager;
import com.slender.model.message.OrderMessage;
import com.slender.service.interfase.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {
    private final OrderService orderService;
    private final JsonParserManager jsonParser;

    @RabbitListener(queues = "order")
    public void updateStatus(String message) {
        try {
            OrderMessage orderMessage = jsonParser.parse(message, OrderMessage.class);
            orderService.updateStatus(orderMessage.bid(), orderMessage.status());
        } catch (Exception e) {
            log.error("消息处理失败: {}", message, e);
        }
    }
}
