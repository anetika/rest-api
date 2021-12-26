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

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcGiftCertificateDao implements GiftCertificateDao {

    private static final String INSERT_CERTIFICATE_SQL = "INSERT INTO gift_certificate (name, description, "
        + "price, duration, create_date, last_update_date) VALUES(?,?,?,?,?,?)";
    private static final String GET_CERTIFICATE_BY_ID_SQL = "SELECT id, name, description, "
        + "price, duration, create_date, last_update_date FROM gift_certificate WHERE id = ?";
    private static final String DELETE_CERTIFICATE_BY_ID_SQL = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String DELETE_ALL_CERTIFICATES_SQL = "DELETE FROM gift_certificate";
    private static final String UPDATE_CERTIFICATE_SQL = "UPDATE gift_certificate SET gift_certificate.name = ?, description = ?, "
        + "price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";
    private static final String INSERT_CERTIFICATE_TAG_CONNECTION_SQL = "INSERT INTO gift_certificate_tag (certificate_id, tag_id) " +
            "VALUES(?, ?)";
    private static final String DELETE_CERTIFICATE_TAG_CONNECTION_SQL = "DELETE FROM gift_certificate_tag WHERE certificate_id = ? AND tag_id = ?";
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
                item.setId(keyHolder.getKey().longValue());
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
            jdbcTemplate.update(UPDATE_CERTIFICATE_SQL, updatedCertificate.getName(), updatedCertificate.getDescription(),
                    updatedCertificate.getPrice(), updatedCertificate.getDuration(), updatedCertificate.getCreateDate(),
                    updatedCertificate.getLastUpdateDate(), id);
            return updatedCertificate;
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

    @Override
    public void deleteGiftCertificateTagConnection(long certificateId, long tagId) {
        try{
            jdbcTemplate.update(DELETE_CERTIFICATE_TAG_CONNECTION_SQL, certificateId, tagId);
        } catch (DataAccessException e){
            throw new RepositoryException("Unable to handle deleteGiftCertificateTagConnection request in JdbcGiftCertificateDao", e);
        }
    }

}
