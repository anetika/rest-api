package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * The interface that contains functionality for UserDao
 */
public interface UserDao {
    /**
     * Gets a user by highest cost of all orders.
     *
     * @return the user with highest cost of all orders
     */
    long getMostWidelyUsedTagOfUserByHighestCostOfAllOrders();

    /**
     * Saves a user.
     *
     * @param user the user
     * @return the user
     */
    User save(User user);

    /**
     * Finds a user by id.
     *
     * @param id the id of the user
     * @return the user
     */
    Optional<User> findById(long id);

    /**
     * Finds all users.
     *
     * @param page the page
     * @param size the size
     * @return the list of users
     */
    List<User> findAll(int page, int size);
}
