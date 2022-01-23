package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public long getMostWidelyUsedTagOfUserByHighestCostOfAllOrders() {
        Query query = entityManager.createNativeQuery("SELECT certificate_tag.tag_id " +
                "FROM certificate_tag WHERE certificate_tag.certificate_id IN " +
                "(SELECT gift_certificates.certificate_id FROM gift_certificates JOIN orders " +
                "ON orders.certificate_id = gift_certificates.certificate_id WHERE orders.order_id IN " +
                "(SELECT orders.order_id FROM orders JOIN users_orders ON orders.order_id = users_orders.orders_order_id " +
                "WHERE users_orders.user_user_id = (SELECT users_orders.user_user_id FROM users_orders JOIN orders " +
                "ON users_orders.orders_order_id = orders.order_id GROUP BY users_orders.user_user_id " +
                "ORDER BY SUM(orders.total_price) DESC LIMIT 1))) GROUP BY certificate_tag.tag_id " +
                "ORDER BY COUNT(certificate_tag.tag_id) DESC LIMIT 1");
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findAll(int page, int size) {
        Query query = entityManager.createQuery("SELECT user FROM User user");
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }
}
