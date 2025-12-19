package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.order.OrderCreateRequest;
import com.slender.dto.order.OrderUpdateRequest;
import com.slender.entity.Order;
import com.slender.enumeration.order.OrderStatus;
import com.slender.vo.ListData;
import jakarta.servlet.http.HttpServletResponse;

public interface OrderService extends IService<Order> {
    ListData<Order> getList(Long uid, Boolean order);

    void create(Long uid, OrderCreateRequest request);

    Order get(Long bid, Long uid);

    void delete(Long bid);

    void update(Long bid, Long uid, OrderUpdateRequest request);

    void updateStatus(Long bid, Long uid, OrderStatus status);

    void export(HttpServletResponse response);
}
