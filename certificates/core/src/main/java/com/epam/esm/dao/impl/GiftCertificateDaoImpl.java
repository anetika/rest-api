package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private static final String TAG = "tag";
    private static final String SORT = "sort";
    private static final String SEARCH = "search";
    private static final String ASC = "ASC";
    private static final String NAME = "name";
    private static final String TAGS = "tags";

    @PersistenceContext
    private final EntityManager entityManager;

    private final TagDao tagDao;

    public GiftCertificateDaoImpl(EntityManager entityManager, TagDao tagDao) {
        this.entityManager = entityManager;
        this.tagDao = tagDao;
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
    public Page<GiftCertificate> findAll(Map<String, String> params, Pageable pageable) {
        CriteriaQuery<GiftCertificate> criteriaQuery = createGetAllQuery(params);
        Query query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
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

    @Override
    public List<GiftCertificate> findGiftCertificatesByTags(List<Tag> tags, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        List<Predicate> predicates = new ArrayList<>();
        for (var tag : tags) {
            predicates.add(criteriaBuilder.isMember(tag, root.get("tags")));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
    }

    private CriteriaQuery<GiftCertificate> createGetAllQuery(Map<String, String> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        List<Predicate> predicates = new ArrayList<>();

        if (params.get(SORT).equals(ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(NAME)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(NAME)));
        }

        if (!params.get(TAG).isEmpty()) {
            Optional<Tag> optionalTag = tagDao.findTagByName(params.get(TAG));
            if (optionalTag.isPresent()) {
                Tag tag = optionalTag.get();
                predicates.add(criteriaBuilder.isMember(tag, root.get(TAGS)));
            } else {
                throw new IllegalArgumentException("Tag doesn't exist");
            }
        }

        if (!params.get(SEARCH).isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get(NAME),"%" + params.get(SEARCH) + "%"));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return criteriaQuery;
    }
}
