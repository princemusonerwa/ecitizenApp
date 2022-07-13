package com.minaloc.gov.service;

import com.minaloc.gov.domain.*; // for static metamodels
import com.minaloc.gov.domain.Umurimo;
import com.minaloc.gov.repository.UmurimoRepository;
import com.minaloc.gov.service.criteria.UmurimoCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Umurimo} entities in the database.
 * The main input is a {@link UmurimoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Umurimo} or a {@link Page} of {@link Umurimo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UmurimoQueryService extends QueryService<Umurimo> {

    private final Logger log = LoggerFactory.getLogger(UmurimoQueryService.class);

    private final UmurimoRepository umurimoRepository;

    public UmurimoQueryService(UmurimoRepository umurimoRepository) {
        this.umurimoRepository = umurimoRepository;
    }

    /**
     * Return a {@link List} of {@link Umurimo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Umurimo> findByCriteria(UmurimoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Umurimo> specification = createSpecification(criteria);
        return umurimoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Umurimo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Umurimo> findByCriteria(UmurimoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Umurimo> specification = createSpecification(criteria);
        return umurimoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UmurimoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Umurimo> specification = createSpecification(criteria);
        return umurimoRepository.count(specification);
    }

    /**
     * Function to convert {@link UmurimoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Umurimo> createSpecification(UmurimoCriteria criteria) {
        Specification<Umurimo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Umurimo_.id));
            }
            if (criteria.getUmurimo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUmurimo(), Umurimo_.umurimo));
            }
            if (criteria.getUrwego() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrwego(), Umurimo_.urwego));
            }
        }
        return specification;
    }
}
