package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface GiftCertificateDao extends PagingAndSortingRepository<GiftCertificate, Long>, JpaRepository<GiftCertificate, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE GiftCertificate certificate SET certificate.duration = :duration, certificate.lastUpdateDate = :date " +
            "WHERE certificate.id = :id")
    void updateCertificateDuration(
            @Param(value = "id") long id,
            @Param(value = "duration") int duration,
            @Param(value = "date")LocalDateTime date
            );

    @Modifying
    @Transactional
    @Query("UPDATE GiftCertificate certificate SET certificate.price = :price, certificate.lastUpdateDate = :date " +
            "WHERE certificate.id = :id")
    void updateCertificatePrice(
            @Param(value = "id") long id,
            @Param(value = "price")BigDecimal price,
            @Param(value = "date")LocalDateTime date
    );
}
