package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface that contains functionality for OrderDao
 */
@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
    /**
     * Finds an order by id.
     *
     * @param id the id of the order
     * @return the order
     */
    Optional<Order> findById(long id);
}
