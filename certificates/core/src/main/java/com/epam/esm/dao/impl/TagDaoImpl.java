package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        Query query = entityManager.createQuery("SELECT tag FROM Tag tag WHERE tag.name = :name");
        query.setParameter("name", name);
        try {
            return Optional.of((Tag) query.getSingleResult());
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public List<Tag> findAll(int page, int size) {
        Query query = entityManager.createQuery("SELECT tag FROM Tag tag");
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void deleteById(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
    }

    @Override
    public void deleteAll() {
        Query query = entityManager.createQuery("DELETE FROM Tag tag");
        query.executeUpdate();
    }
}
