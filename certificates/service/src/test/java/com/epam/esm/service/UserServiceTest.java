package com.epam.esm.service;


import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private UserConverter userConverter;

    private final List<User> users = new ArrayList<>();
    private final List<UserDto> userDtos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        UserConverter userConverter = new UserConverter(new OrderConverter(new GiftCertificateConverter(new TagConverter())));
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("Ann");
        user1.setLastName("Ksenevich");
        user1.setEmail("anna.ksenevich@mail.ru");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Kate");
        user2.setLastName("Kislyak");
        user2.setEmail("katekis@mail.ru");

        users.add(user1);
        users.add(user2);

        UserDto dto1 = userConverter.convertEntityToDto(user1);
        UserDto dto2 = userConverter.convertEntityToDto(user2);

        userDtos.add(dto1);
        userDtos.add(dto2);
    }

    @Test
    public void add_UserWithValidInfo_Success() {
        when(userConverter.convertDtoToEntity(userDtos.get(0))).thenReturn(users.get(0));
        when(userDao.save(any())).thenReturn(users.get(0));
        when(userConverter.convertEntityToDto(users.get(0))).thenReturn(userDtos.get(0));
        UserDto userDto = userService.add(userDtos.get(0));
        assertEquals(userDto, userDtos.get(0));
    }

    @Test
    public void getById_ValidId_Success() {
        when(userDao.findById(1L)).thenReturn(Optional.of(users.get(0)));
        when(userConverter.convertEntityToDto(users.get(0))).thenReturn(userDtos.get(0));
        UserDto userDto = userService.getById(1);
        assertEquals(userDto, userDtos.get(0));
    }

    @Test
    public void getAll_Success() {
        when(userDao.findAll(PageRequest.of(0, 2))).thenReturn(new PageImpl<>(users));
        when(userConverter.convertEntityToDto(users.get(0))).thenReturn(userDtos.get(0));
        when(userConverter.convertEntityToDto(users.get(1))).thenReturn(userDtos.get(1));
        Page<UserDto> page = userService.getAll(PageRequest.of(0, 2));
        assertEquals(page.getContent(), userDtos);
    }
}
