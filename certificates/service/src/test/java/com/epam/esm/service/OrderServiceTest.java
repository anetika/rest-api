package com.epam.esm.service;


import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderInfoDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private UserDao userDao;

    @Mock
    private OrderConverter orderConverter;

    private final List<Order> orders = new ArrayList<>();

    private final List<OrderDto> orderDtos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        OrderConverter orderConverter = new OrderConverter(new GiftCertificateConverter(new TagConverter()));

        GiftCertificateDto dto1 = new GiftCertificateDto();
        dto1.setId(1);
        dto1.setName("Holiday");
        dto1.setDescription("description 1");
        dto1.setPrice(new BigDecimal(1000));
        dto1.setDuration(10);
        dto1.setCreateDate(LocalDateTime.now());
        dto1.setLastUpdateDate(LocalDateTime.now());
        dto1.setTags(new HashSet<>());

        GiftCertificate certificate1 = new GiftCertificate();
        certificate1.setId(1);
        certificate1.setName("Holiday");
        certificate1.setDescription("description 1");
        certificate1.setPrice(new BigDecimal(1000));
        certificate1.setDuration(10);
        certificate1.setCreateDate(LocalDateTime.now());
        certificate1.setLastUpdateDate(LocalDateTime.now());
        certificate1.setTags(new HashSet<>());

        Order order1 = new Order();
        order1.setId(1);
        order1.setTotalPrice(new BigDecimal(1000));
        order1.setOrderDate(LocalDateTime.now());
        order1.setCertificate(certificate1);

        Order order2 = new Order();
        order2.setId(2);
        order2.setTotalPrice(new BigDecimal(2000));
        order2.setOrderDate(LocalDateTime.now());
        order2.setCertificate(certificate1);

        orders.add(order1);
        orders.add(order2);

        OrderDto orderDto1 = orderConverter.convertEntityToDto(order1);
        OrderDto orderDto2 = orderConverter.convertEntityToDto(order2);

        orderDtos.add(orderDto1);
        orderDtos.add(orderDto2);
    }

    @Test
    public void getOrdersByUserId_Success() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Ann");
        user.setLastName("Ksenevich");
        user.setEmail("anna.ksenevich@mail.ru");
        user.setOrders(orders);
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderConverter.convertEntityToDto(orders.get(0))).thenReturn(orderDtos.get(0));
        when(orderConverter.convertEntityToDto(orders.get(1))).thenReturn(orderDtos.get(1));
        Set<OrderDto> resultDtos = orderService.getOrdersByUserId(1);
        assertEquals(new ArrayList<>(resultDtos), orderDtos);
    }

    @Test
    public void getOrderInfoByUserId_Success() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Ann");
        user.setLastName("Ksenevich");
        user.setEmail("anna.ksenevich@mail.ru");
        user.setOrders(orders);
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        OrderInfoDto dto = new OrderInfoDto();
        dto.setPrice(orders.get(0).getTotalPrice());
        dto.setOrderDate(orders.get(0).getOrderDate());
        OrderInfoDto resultDto = orderService.getOrderInfoByUserId(1, 1);
        assertEquals(resultDto, dto);
    }

    @Test
    public void getOrderById_Success() {
        when(orderDao.findById(1)).thenReturn(Optional.of(orders.get(0)));
        when(orderConverter.convertEntityToDto(orders.get(0))).thenReturn(orderDtos.get(0));
        OrderDto resultDto = orderService.getOrderById(1);
        assertEquals(resultDto, orderDtos.get(0));
    }
}
