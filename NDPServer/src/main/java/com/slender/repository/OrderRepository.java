package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slender.constant.order.OrderColumn;
import com.slender.entity.Order;
import com.slender.enumeration.order.OrderStatus;
import com.slender.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderMapper orderMapper;

    public Optional<Order> findById(Long bid){
        return Optional.ofNullable(orderMapper.selectOne(
                new QueryWrapper<Order>()
                        .eq(OrderColumn.BID,bid)
                        .eq(OrderColumn.STATUS, OrderStatus.PAID_SUCCESS.getValue())
        ));
    }
}
