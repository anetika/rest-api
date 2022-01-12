package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderInfoDto;

import java.util.Set;

public interface OrderService {
    Set<OrderDto> getOrdersByUserId(long id);
    OrderInfoDto getOrderInfoByUserId(long userId, long orderId);
    OrderDto getOrderById(long id);
}
