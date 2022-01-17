package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    long getUserByHighestCostOfAllOrders();
    User save(User user);
    Optional<User> findById(long id);
    List<User> findAll(int page, int size);
}
