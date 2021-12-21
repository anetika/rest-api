package com.epam.esm.repository.impl.mapper;

import com.epam.esm.entity.GiftCertificate;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.time.LocalDateTime;

/**
 * The row mapper for {@link GiftCertificate}.
 */
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    @SneakyThrows
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(rs.getLong(1));
        certificate.setName(rs.getString(2));
        certificate.setDescription(rs.getString(3));
        certificate.setPrice(rs.getBigDecimal(4));
        certificate.setDuration(rs.getInt(5));
        certificate.setCreateDate(rs.getObject(6, LocalDateTime.class));
        certificate.setLastUpdateDate(rs.getObject(7, LocalDateTime.class));
        return certificate;
    }
}
