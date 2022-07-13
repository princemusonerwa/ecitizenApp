package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Complain;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ComplainRepositoryWithBagRelationshipsImpl implements ComplainRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Complain> fetchBagRelationships(Optional<Complain> complain) {
        return complain.map(this::fetchOrganizations);
    }

    @Override
    public Page<Complain> fetchBagRelationships(Page<Complain> complains) {
        return new PageImpl<>(fetchBagRelationships(complains.getContent()), complains.getPageable(), complains.getTotalElements());
    }

    @Override
    public List<Complain> fetchBagRelationships(List<Complain> complains) {
        return Optional.of(complains).map(this::fetchOrganizations).orElse(Collections.emptyList());
    }

    Complain fetchOrganizations(Complain result) {
        return entityManager
            .createQuery(
                "select complain from Complain complain left join fetch complain.organizations where complain is :complain",
                Complain.class
            )
            .setParameter("complain", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Complain> fetchOrganizations(List<Complain> complains) {
        return entityManager
            .createQuery(
                "select distinct complain from Complain complain left join fetch complain.organizations where complain in :complains",
                Complain.class
            )
            .setParameter("complains", complains)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
