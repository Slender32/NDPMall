package com.slender.service.listener

import com.slender.config.manager.JsonParserManager
import com.slender.model.message.OrderMessage
import com.slender.service.interfase.OrderService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class OrderListener(
    private val orderService: OrderService,
    private val jsonParser: JsonParserManager
) {
    private val log = LoggerFactory.getLogger(javaClass)
    @RabbitListener(queues = ["order"])
    fun updateStatus(message: String) {
        try {
            val orderMessage = jsonParser.parse(message, OrderMessage::class.java)
            orderService.updateStatus(orderMessage.bid, orderMessage.uid,orderMessage.status)
        } catch (e: Exception) {
            log.error("消息处理失败: {}", message, e)
        }
    }
}
