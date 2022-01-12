package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, Long> {
    @Query(value = "SELECT users_orders.user_user_id FROM users_orders JOIN orders ON users_orders.orders_order_id = orders.order_id GROUP BY users_orders.user_user_id ORDER BY SUM(orders.total_price) DESC LIMIT 1", nativeQuery = true)
    long getUserByHighestCostOfAllOrders();
}
