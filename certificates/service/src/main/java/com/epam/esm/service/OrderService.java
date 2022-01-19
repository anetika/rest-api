package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.Set;

public interface OrderService {
    Set<OrderDto> getOrdersByUserId(long id);
    OrderDto getOrderByUserId(long userId, long orderId);
    OrderDto getOrderById(long id);
}
