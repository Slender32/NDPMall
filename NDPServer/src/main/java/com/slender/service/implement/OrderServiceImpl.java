package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.entity.Order;
import com.slender.mapper.OrderMapper;
import com.slender.service.interfase.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
