package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.PaginationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final GiftCertificateDao certificateDao;
    private final UserConverter converter;
    private final OrderConverter orderConverter;
    private final TagConverter tagConverter;
    private final PaginationUtil paginationUtil;

    public UserServiceImpl(UserDao userDao, GiftCertificateDao certificateDao, UserConverter converter, OrderConverter orderConverter, TagConverter tagConverter, PaginationUtil paginationUtil) {
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.converter = converter;
        this.orderConverter = orderConverter;
        this.tagConverter = tagConverter;
        this.paginationUtil = paginationUtil;
    }

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        User user = converter.convertDtoToEntity(userDto);
        return converter.convertEntityToDto(userDao.save(user));
    }

    @Override
    public UserDto getById(long id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()){
            return converter.convertEntityToDto(userOptional.get());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    public List<UserDto> getAll(int page, int size) {
        paginationUtil.validatePaginationInfo(page, size);
        List<User> users = userDao.findAll(page, size);
        if (users.isEmpty()){
            throw new ResourceNotFoundException("Resource not found");
        }
        return users.stream().map(converter::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto buyCertificate(long userId, long certificateId) {
        Order order = new Order();
        Optional<GiftCertificate> optionalGiftCertificate = certificateDao.findById(certificateId);
        if (optionalGiftCertificate.isPresent()){
            GiftCertificate certificate = optionalGiftCertificate.get();
            order.setCertificate(certificate);
            order.setTotalPrice(certificate.getPrice());
        } else {
            throw new ResourceNotFoundException("Resource not found");
        }
        order.setOrderDate(LocalDateTime.now());
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        } else {
            User user = optionalUser.get();
            user.getOrders().add(order);
            user = userDao.save(user);
            List<Order> orders = user.getOrders();
            order.setId(orders.get(orders.size() - 1).getId());
        }
        return orderConverter.convertEntityToDto(order);
    }

    @Override
    @Transactional
    public TagDto getMostWidelyUsedTag() {
        long userId = userDao.getUserByHighestCostOfAllOrders();
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Order> orders = user.getOrders();
            Map<Tag, Integer> tagMap = new HashMap<>();
            orders.forEach(a -> a.getCertificate().getTags().forEach(b -> {
                if (tagMap.containsKey(b)) {
                    tagMap.replace(b, tagMap.get(b) + 1);
                } else {
                    tagMap.put(b, 1);
                }
            }));
            int maxValue = Collections.max(tagMap.values());
            Optional<Map.Entry<Tag, Integer>> optional = tagMap.entrySet().stream().filter(a -> a.getValue() == maxValue).findFirst();
            if (optional.isPresent()) {
                Tag tag = optional.get().getKey();
                return tagConverter.convertEntityToDto(tag);
            }
        }
        throw new ResourceNotFoundException("Resource not found");
    }
}
