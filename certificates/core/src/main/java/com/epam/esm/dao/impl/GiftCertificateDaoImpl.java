package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public GiftCertificateDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void updateCertificateDuration(long id, int duration, LocalDateTime date) {
        Query query = entityManager.createQuery("UPDATE GiftCertificate certificate SET certificate.duration = :duration, certificate.lastUpdateDate = :date " +
                "WHERE certificate.id = :id");
        query.setParameter("duration", duration);
        query.setParameter("date", date);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void updateCertificatePrice(long id, BigDecimal price, LocalDateTime date) {
        Query query = entityManager.createQuery("UPDATE GiftCertificate certificate SET certificate.price = :price, certificate.lastUpdateDate = :date " +
                "WHERE certificate.id = :id");
        query.setParameter("price", price);
        query.setParameter("date", date);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public GiftCertificate save(GiftCertificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size) {
        return null;
    }

    @Override
    public void deleteById(long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(giftCertificate);
    }

    @Override
    public void deleteAll() {
        Query query = entityManager.createQuery("DELETE FROM GiftCertificate giftCertificate");
        query.executeUpdate();
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }
}
