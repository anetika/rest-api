package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    @Query(value = "SELECT users_orders.user_user_id FROM users_orders JOIN orders ON users_orders.orders_order_id = orders.order_id GROUP BY users_orders.user_user_id ORDER BY SUM(orders.total_price) DESC LIMIT 1", nativeQuery = true)
    long getUserByHighestCostOfAllOrders();
    User save(User user);
    Optional<User> findById(long id);
    List<User> findAll(int page, int size);
}
