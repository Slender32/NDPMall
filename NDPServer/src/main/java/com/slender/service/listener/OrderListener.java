package com.slender.service.listener;

import com.slender.config.manager.JsonParserManager;
import com.slender.model.message.OrderMessage;
import com.slender.service.interfase.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {
    private final OrderService orderService;
    private final JsonParserManager jsonParser;

    @RabbitListener(queues = "order")
    @Retryable(retryFor = RuntimeException.class, backoff = @Backoff(delay = 1000))
    public void updateStatus(String message) {
        OrderMessage orderMessage = jsonParser.parse(message, OrderMessage.class);
        orderService.updateStatus(orderMessage.bid(), orderMessage.status());
    }

    @Recover
    public void recover(Exception ex, String message) {
        log.error("消息处理失败: {}", message, ex);
    }

}
