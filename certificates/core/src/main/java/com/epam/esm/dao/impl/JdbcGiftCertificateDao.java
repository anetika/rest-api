package com.epam.esm.dao.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.impl.builder.GiftCertificateRequestBuilder;
import com.epam.esm.dao.impl.mapper.GiftCertificateRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class JdbcGiftCertificateDao implements GiftCertificateDao {

    private static final String INSERT_CERTIFICATE_SQL = "INSERT INTO gift_certificate (name, description, "
        + "price, duration, create_date, last_update_date) VALUES(?,?,?,?,?,?)";
    private static final String GET_CERTIFICATE_BY_ID_SQL = "SELECT id, name, description, "
        + "price, duration, create_date, last_update_date FROM gift_certificate WHERE id = ?";
    private static final String GET_ALL_CERTIFICATES_SQL = "SELECT id, name, description, "
            + "price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String DELETE_CERTIFICATE_BY_ID_SQL = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String DELETE_ALL_CERTIFICATES_SQL = "DELETE FROM gift_certificate";
    private static final String UPDATE_CERTIFICATE_SQL = "UPDATE gift_certificate SET gift_certificate.name = ?, description = ?, "
        + "price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";
    private static final String INSERT_CERTIFICATE_TAG_CONNECTION_SQL = "INSERT INTO gift_certificate_tag (certificate_id, tag_id) " +
            "VALUES(?, ?)";
    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateRowMapper certificateRowMapper;

    public JdbcGiftCertificateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateRowMapper = new GiftCertificateRowMapper();
    }

    @Override
    public GiftCertificate add(GiftCertificate item) throws RepositoryException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_CERTIFICATE_SQL, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, item.getName());
                statement.setString(2, item.getDescription());
                statement.setBigDecimal(3, item.getPrice());
                statement.setInt(4, item.getDuration());
                statement.setObject(5, item.getCreateDate());
                statement.setObject(6, item.getLastUpdateDate());
                return statement;
            }, keyHolder);

            if (keyHolder.getKey() != null){
                item.setId((BigInteger.valueOf(Long.valueOf(keyHolder.getKey().toString()))).longValue());
            }
            return item;
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle add request in JdbcGiftCertificateRepository", e);
        }
    }

    @Override
    public GiftCertificate getById(long id) throws RepositoryException, ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(GET_CERTIFICATE_BY_ID_SQL, certificateRowMapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Resource not found in JdbcGiftCertificateRepository", e);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle getById request in JdbcGiftCertificateRepository", e);
        }
    }

    @Override
    public List<GiftCertificate> getAll(Map<String, String> params) throws RepositoryException {
        try {
            String query = GiftCertificateRequestBuilder.createGetAllRequest(params);
            return jdbcTemplate.query(query, certificateRowMapper);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle getAll request in JdbcGiftCertificateRepository", e);
        }
    }

    @Override
    public void delete(long id) throws RepositoryException {
        try {
            jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID_SQL, id);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle delete request in JdbcGiftCertificateRepository", e);
        }
    }

    @Override
    public void deleteAll() throws RepositoryException {
        try {
            jdbcTemplate.update(DELETE_ALL_CERTIFICATES_SQL);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle deleteAll request in JdbcGiftCertificateRepository", e);
        }
    }

    @Override
    public GiftCertificate update(long id, GiftCertificate updatedCertificate) throws RepositoryException, ResourceNotFoundException {
        try {

            GiftCertificate initialCertificate = getById(id);
            String initialName = initialCertificate.getName();
            String updatedName = updatedCertificate.getName();
            if (updatedName != null && !Objects.equals(initialName, updatedName)) {
                initialCertificate.setName(updatedName);
            }

            String initialDescription = initialCertificate.getDescription();
            String updatedDescription = updatedCertificate.getDescription();
            if (updatedDescription != null && !Objects.equals(initialDescription, updatedDescription)) {
                initialCertificate.setDescription(updatedDescription);
            }

            BigDecimal initialPrice = initialCertificate.getPrice();
            BigDecimal updatedPrice = updatedCertificate.getPrice();
            if (updatedPrice != null && !Objects.equals(initialPrice, updatedPrice)) {
                initialCertificate.setPrice(updatedPrice);
            }

            int initialDuration = initialCertificate.getDuration();
            int updatedDuration = updatedCertificate.getDuration();
            if (updatedDuration != 0 && !Objects.equals(initialDuration, updatedDuration)) {
                initialCertificate.setDuration(updatedDuration);
            }

            LocalDateTime initialCreateDate = initialCertificate.getCreateDate();
            LocalDateTime updatedCreateDate = initialCertificate.getCreateDate();
            if (updatedCreateDate != null && initialCreateDate != updatedCreateDate) {
                initialCertificate.setCreateDate(updatedCreateDate);
            }

            LocalDateTime initialLastUpdateDate = initialCertificate.getLastUpdateDate();
            LocalDateTime updatedLastUpdateDate = updatedCertificate.getLastUpdateDate();
            if (updatedLastUpdateDate != null && initialLastUpdateDate != updatedLastUpdateDate) {
                initialCertificate.setLastUpdateDate(updatedLastUpdateDate);
            }

            if (updatedCertificate.getTags() != null){
                initialCertificate.setTags(updatedCertificate.getTags());
            }

            jdbcTemplate.update(UPDATE_CERTIFICATE_SQL, initialCertificate.getName(), initialCertificate.getDescription(),
                    initialCertificate.getPrice(), initialCertificate.getDuration(), initialCertificate.getCreateDate(),
                    initialCertificate.getLastUpdateDate(), id);
            return initialCertificate;

        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Resource not found in JdbcGiftCertificateRepository", e);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle update request in JdbcGiftCertificateRepository", e);
        }
    }

    @Override
    public void addGiftCertificateTagConnection(long certificateId, long tagId) throws RepositoryException {
        try{
            jdbcTemplate.update(INSERT_CERTIFICATE_TAG_CONNECTION_SQL, certificateId, tagId);
        } catch (DataAccessException e){
            throw new RepositoryException("Unable to handle addCertificateTagConnection request in JdbcGiftCertificateRepository", e);
        }
    }

}
