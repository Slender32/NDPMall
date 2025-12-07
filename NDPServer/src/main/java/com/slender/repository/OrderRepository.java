package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.order.OrderColumn;
import com.slender.dto.order.OrderUpdateRequest;
import com.slender.entity.Order;
import com.slender.enumeration.order.OrderStatus;
import com.slender.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderMapper orderMapper;

    public List<Order> getList(Long uid, Boolean order) {
        return orderMapper.selectList(
                new QueryWrapper<Order>()
                        .eq(OrderColumn.UID, uid)
                        .orderByDesc(order,OrderColumn.CREATE_TIME)
        );
    }

    public Optional<Order> get(Long bid) {
        return Optional.ofNullable(orderMapper.selectById(bid));
    }

    public void update(Long bid, OrderUpdateRequest request) {
        if(request.getAid() == null && request.getQuantity() == null && request.getTotalAmount() == null) return;
        orderMapper.update(
                new UpdateWrapper<Order>()
                        .eq(OrderColumn.BID, bid)
                        .set(request.getAid() != null, OrderColumn.AID, request.getAid())
                        .set(request.getQuantity() != null, OrderColumn.QUANTITY, request.getQuantity())
                        .set(request.getTotalAmount() != null, OrderColumn.TOTAL_AMOUNT, request.getTotalAmount())
        );
    }

    public void updateStatus(Long bid, OrderStatus status) {
        orderMapper.update(
                new UpdateWrapper<Order>()
                        .eq(OrderColumn.BID, bid)
                        .set(OrderColumn.STATUS, status)
        );
    }
}
