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
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setOrderDtoList(user.getOrders() == null ? new ArrayList<>() : user.getOrders().stream().map(orderConverter::convertEntityToDto).collect(Collectors.toList()));
        return dto;
    }

    public User convertDtoToEntity(UserDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setOrders(dto.getOrderDtoList() == null ? new ArrayList<>() : dto.getOrderDtoList().stream().map(orderConverter::convertDtoToEntity).collect(Collectors.toList()));
        return user;
    }
}
