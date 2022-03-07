package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.RoleDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.entity.*;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final TagDao tagDao;
    private final RoleDao roleDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserDao userDao, GiftCertificateDao certificateDao, UserConverter converter, OrderConverter orderConverter, TagConverter tagConverter, TagDao tagDao, RoleDao roleDao, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.certificateDao = certificateDao;
        this.converter = converter;
        this.orderConverter = orderConverter;
        this.tagConverter = tagConverter;
        this.tagDao = tagDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        User user = converter.convertDtoToEntity(userDto);
        return converter.convertEntityToDto(userDao.save(user));
    }

    @Override
    public UserDto register(RegistrationDto requestDto) {
        User user = new User();
        Role roleUser = roleDao.findByRoleName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(userRoles);
        user.setUsername(requestDto.getUsername());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
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
    public Page<UserDto> getAll(Pageable pageable) {
        Page<User> users = userDao.findAll(pageable);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return new PageImpl<>(users.getContent().stream().map(converter::convertEntityToDto).collect(Collectors.toList()));
    }

    @Override
    public UserDto findUserByUserName(String username) {
        Optional<User> result = userDao.findUserByUsername(username);
        if (result.isPresent()) {
            return  converter.convertEntityToDto(result.get());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    @Transactional
    public OrderDto buyCertificate(BuyCertificateDto buyCertificateDto) {
        Order order = new Order();
        Optional<GiftCertificate> optionalGiftCertificate = certificateDao.findById(buyCertificateDto.getCertificateId());
        if (optionalGiftCertificate.isPresent()){
            GiftCertificate certificate = optionalGiftCertificate.get();
            order.setCertificate(certificate);
            order.setTotalPrice(certificate.getPrice());
        } else {
            throw new ResourceNotFoundException("Resource not found");
        }
        order.setOrderDate(LocalDateTime.now());
        Optional<User> optionalUser = userDao.findById(buyCertificateDto.getUserId());
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
        long tagId = userDao.getMostWidelyUsedTagOfUserByHighestCostOfAllOrders();
        Optional<Tag> optionalTag = tagDao.findById(tagId);
        if (optionalTag.isPresent()){
            return tagConverter.convertEntityToDto(optionalTag.get());
        }
        throw new ResourceNotFoundException("Resource not found");
    }

    @Override
    public AuthenticationResponseDto login(AuthenticationDto requestDto) {
        String username = requestDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
        Optional<User> optionalUser = userDao.findUserByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        String token = jwtTokenProvider.createToken(optionalUser.get());
        return AuthenticationResponseDto.builder()
                .username(username)
                .token(token)
                .build();
    }
}
