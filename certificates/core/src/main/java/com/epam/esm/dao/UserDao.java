package com.epam.esm.dao;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface that contains functionality for UserDao
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String name);

    /**
     * Gets a user by highest cost of all orders.
     *
     * @return the user with highest cost of all orders
     */
    @Query(value = "SELECT certificate_tag.tag_id " +
                    "FROM certificate_tag WHERE certificate_tag.certificate_id IN " +
                    "(SELECT gift_certificates.certificate_id FROM gift_certificates JOIN orders " +
                    "ON orders.certificate_id = gift_certificates.certificate_id WHERE orders.order_id IN " +
                    "(SELECT orders.order_id FROM orders JOIN users_orders ON orders.order_id = users_orders.orders_order_id " +
                    "WHERE users_orders.user_user_id = (SELECT users_orders.user_user_id FROM users_orders JOIN orders " +
                    "ON users_orders.orders_order_id = orders.order_id GROUP BY users_orders.user_user_id " +
                    "ORDER BY SUM(orders.total_price) DESC LIMIT 1))) GROUP BY certificate_tag.tag_id " +
                    "ORDER BY COUNT(certificate_tag.tag_id) DESC LIMIT 1", nativeQuery = true)
    long getMostWidelyUsedTagOfUserByHighestCostOfAllOrders();
}
