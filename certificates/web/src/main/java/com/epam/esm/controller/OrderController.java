package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.HateoasUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final HateoasUtil hateoasUtil;

    public OrderController(OrderService orderService, HateoasUtil hateoasUtil) {
        this.orderService = orderService;
        this.hateoasUtil = hateoasUtil;
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Set<OrderDto>> getOrdersByUserId(@PathVariable long userId) {
        Set<OrderDto> orderDtos = orderService.getOrdersByUserId(userId);
        orderDtos.forEach(hateoasUtil::attacheOrderLink);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable long id) {
        OrderDto orderDto = orderService.getOrderById(id);
        hateoasUtil.attacheOrderLink(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable long userId, @PathVariable long orderId) {
        return new ResponseEntity<>(orderService.getOrderByUserId(userId, orderId), HttpStatus.OK);
    }
}
