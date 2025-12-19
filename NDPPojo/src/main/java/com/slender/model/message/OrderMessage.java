package com.slender.model.message;

import com.slender.enumeration.order.OrderStatus;

public record OrderMessage(
    Long bid,
    Long uid,
    OrderStatus status
){}


