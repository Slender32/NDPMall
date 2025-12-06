package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.order.OrderCreateRequest;
import com.slender.dto.order.OrderUpdateRequest;
import com.slender.entity.Order;
import com.slender.enumeration.order.OrderStatus;
import com.slender.vo.ListData;

public interface OrderService extends IService<Order> {
    ListData<Order> getList(Long uid, Boolean order);

    void create(Long uid, OrderCreateRequest request);

    Order get(Long oid);

    void delete(Long oid);

    void update(Long bid, OrderUpdateRequest request);

    void updateStatus(Long bid, OrderStatus status);
}
