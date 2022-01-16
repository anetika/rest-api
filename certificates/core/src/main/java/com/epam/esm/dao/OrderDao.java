package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDao {
    Optional<Order> findById(long id);
}
