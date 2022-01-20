package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.Set;

/**
 * The interface that contains functionality for OrderService.
 */
public interface OrderService {
    /**
     * Gets order dtos by user id.
     *
     * @param id the id of the user
     * @return the order dtos
     */
    Set<OrderDto> getOrdersByUserId(long id);

    /**
     * Gets order dto by user id.
     *
     * @param userId  the user id
     * @param orderId the order id
     * @return the order dto
     */
    OrderDto getOrderByUserId(long userId, long orderId);

    /**
     * Gets order dto by id.
     *
     * @param id the id of the order dto
     * @return the order dto
     */
    OrderDto getOrderById(long id);
}
