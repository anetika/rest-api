package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderConverter converter;
    private final UserDao userDao;

    public OrderServiceImpl(OrderDao orderDao, OrderConverter converter, UserDao userDao) {
        this.orderDao = orderDao;
        this.converter = converter;
        this.userDao = userDao;
    }

    @Override
    public Set<OrderDto> getOrdersByUserId(long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getOrders().stream().map(converter::convertEntityToDto).collect(Collectors.toSet());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    public OrderDto getOrderByUserId(long userId, long orderId) {
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Order> optionalOrder = user.getOrders().stream().filter(a -> a.getId() == orderId).findFirst();
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                return converter.convertEntityToDto(order);
            }
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    public OrderDto getOrderById(long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return converter.convertEntityToDto(order);
        }
        throw new ResourceNotFoundException("Resource not found");
    }
}
