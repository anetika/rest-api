package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderInfoDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import org.springframework.dao.DataAccessException;
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
        try{
            Optional<User> optionalUser = userDao.findById(id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return user.getOrders().stream().map(converter::convertEntityToDto).collect(Collectors.toSet());
            }
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e){
            throw new ServiceException("Unable to handle getOrdersByCertificateId request in OrderServiceImpl", e);
        }
    }

    @Override
    public OrderInfoDto getOrderInfoByUserId(long userId, long orderId) {
        try {
            Optional<User> optionalUser = userDao.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Optional<Order> optionalOrder = user.getOrders().stream().filter(a -> a.getId() == orderId).findFirst();
                if (optionalOrder.isPresent()) {
                    Order order = optionalOrder.get();
                    OrderInfoDto orderInfoDto = new OrderInfoDto();
                    orderInfoDto.setOrderDate(order.getOrderDate());
                    orderInfoDto.setPrice(order.getTotalPrice());
                    return orderInfoDto;
                }
            }
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getOrderInfoByUserId request in OrderServiceImpl", e);
        }
    }

    @Override
    public OrderDto getOrderById(long id) {
        try {
            Optional<Order> optionalOrder = orderDao.findById(id);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                return converter.convertEntityToDto(order);
            }
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e) {
            throw new ServiceException("Unable to handle getOrderById request in OrderServiceImpl", e);
        }
    }
}
