package com.epam.esm.converter;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    private final OrderConverter orderConverter;

    public UserConverter(OrderConverter orderConverter) {
        this.orderConverter = orderConverter;
    }

    public UserDto convertEntityToDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .orderDtoList(user.getOrders() == null ? new ArrayList<>() : user.getOrders()
                        .stream().map(orderConverter::convertEntityToDto).collect(Collectors.toList()))
                .build();
    }

    public User convertDtoToEntity(UserDto dto){
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .orders(dto.getOrderDtoList() == null ? new ArrayList<>() : dto.getOrderDtoList()
                        .stream().map(orderConverter::convertDtoToEntity).collect(Collectors.toList()))
                .build();
    }
}
