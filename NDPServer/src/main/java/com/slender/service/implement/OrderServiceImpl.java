package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.config.manager.JsonParserManager;
import com.slender.constant.other.RabbitMQConstant;
import com.slender.dto.order.OrderCreateRequest;
import com.slender.dto.order.OrderUpdateRequest;
import com.slender.entity.Order;
import com.slender.enumeration.order.OrderStatus;
import com.slender.exception.order.OrderNotFoundException;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.mapper.OrderMapper;
import com.slender.model.message.OrderMessage;
import com.slender.repository.OrderRepository;
import com.slender.repository.ProductRepository;
import com.slender.repository.UserRepository;
import com.slender.service.interfase.OrderService;
import com.slender.vo.ListData;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final JsonParserManager jsonParserManager;
    @Override
    public ListData<Order> getList(Long uid, Boolean order) {
        List<Order> orders = orderRepository.getList(uid, order);
        return new ListData<>(orders.size(),orders);
    }

    @Override
    public void create(Long uid, OrderCreateRequest request) {
        this.save(new Order(uid,request));
    }

    @Override
    public Order get(Long bid) {
        return orderRepository.get(bid)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void delete(Long bid) {
        this.removeById(bid);
    }

    @Override
    public void update(Long bid, OrderUpdateRequest request) {
        orderRepository.update(bid,request);
        if(request.getStatus() != null) {
            String message=jsonParserManager.format(new OrderMessage(bid,request.getStatus()));
            rabbitTemplate.convertAndSend(RabbitMQConstant.ORDER_EXCHANGE, RabbitMQConstant.ORDER_STATUS_ROUTING_KEY, message);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long bid, OrderStatus status){
        Order order = orderRepository.get(bid).orElseThrow(OrderNotFoundException::new);
        BigDecimal price = productRepository.findById(order.getPid()).orElseThrow(ProductNotFoundException::new).getPrice();
        OrderStatus oldStatus = order.getStatus();
        if(status==OrderStatus.PAID_SUCCESS && (oldStatus==OrderStatus.UNPAID || oldStatus==OrderStatus.PAID_FAIL)){
            userRepository.updateBalance(order.getUid(),price.negate());
            productRepository.updateRemain(order.getPid(),-order.getQuantity());
        }else if((status==OrderStatus.PAID_FAIL || oldStatus==OrderStatus.UNPAID) || oldStatus==OrderStatus.PAID_SUCCESS){
            userRepository.updateBalance(order.getUid(),price);
            productRepository.updateRemain(order.getPid(),order.getQuantity());
        }
        orderRepository.updateStatus(bid,status);
    }
}
