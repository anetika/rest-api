package com.epam.esm.dao.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.mapper.TagRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcTagDao implements TagDao {

    private static final String INSERT_TAG_SQL = "INSERT INTO tag (name) VALUES(?)";
    private static final String GET_TAG_BY_ID_SQL = "SELECT id, name FROM tag WHERE id = ?";
    private static final String GET_ALL_TAGS_SQL = "SELECT id, name FROM tag";
    private static final String DELETE_TAG_BY_ID_SQL = "DELETE FROM tag WHERE id = ?";
    private static final String DELETE_ALL_TAGS_SQL = "DELETE FROM tag";
    private static final String GET_TAG_BY_NAME_SQL = "SELECT id, name FROM tag WHERE name = ?";
    private static final String GET_TAGS_BY_CERTIFICATE_ID = "SELECT tag.id, tag.name FROM tag JOIN gift_certificate_tag ON " +
            "tag.id = gift_certificate_tag.tag_id WHERE gift_certificate_tag.certificate_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    public JdbcTagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = new TagRowMapper();
    }

    @Override
    public Tag add(Tag item) throws RepositoryException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_TAG_SQL, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, item.getName());
                return statement;
            }, keyHolder);

            if (keyHolder.getKey() != null){
                item.setId((BigInteger.valueOf(Long.valueOf(keyHolder.getKey().toString()))).longValue());
            }
            return item;
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle add request in JdbcTagRepository", e);
        }
    }

    @Override
    public Tag getById(long id) throws RepositoryException, ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(GET_TAG_BY_ID_SQL, tagRowMapper, id);
        }catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Resource not found");
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle getById request in JdbcTagRepository", e);
        }
    }

    @Override
    public List<Tag> getAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(GET_ALL_TAGS_SQL, tagRowMapper);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle getAll request in JdbcTagRepository", e);
        }

    }

    @Override
    public void delete(long id) throws RepositoryException {
        try {
            jdbcTemplate.update(DELETE_TAG_BY_ID_SQL, id);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle delete request in JdbcTagRepository", e);
        }
    }

    @Override
    public void deleteAll() throws RepositoryException {
        try {
            jdbcTemplate.update(DELETE_ALL_TAGS_SQL);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle deleteAll request in JdbcTagRepository", e);
        }
    }

    @Override
    public Tag getByName(String name) throws RepositoryException, ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(GET_TAG_BY_NAME_SQL, tagRowMapper, name);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResourceNotFoundException("Resource not found in JdbcTagRepository", e);
        } catch (DataAccessException e) {
            throw new RepositoryException("Unable to handle getByName request in JdbcTagRepository", e);
        }
    }

    @Override
    public List<Tag> getAllTagsByCertificateId(long id) throws RepositoryException {
        try{
            return jdbcTemplate.query(GET_TAGS_BY_CERTIFICATE_ID, tagRowMapper, id);
        } catch (DataAccessException e){
            throw new RepositoryException("Unable to handle getAllTagsByCertificateId request in JdbcTagRepository");
        }
    }
}
