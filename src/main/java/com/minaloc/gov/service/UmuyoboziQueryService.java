package com.minaloc.gov.service;

import com.minaloc.gov.domain.*; // for static metamodels
import com.minaloc.gov.domain.Umuyobozi;
import com.minaloc.gov.repository.UmuyoboziRepository;
import com.minaloc.gov.service.criteria.UmuyoboziCriteria;
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
 * Service for executing complex queries for {@link Umuyobozi} entities in the database.
 * The main input is a {@link UmuyoboziCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Umuyobozi} or a {@link Page} of {@link Umuyobozi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UmuyoboziQueryService extends QueryService<Umuyobozi> {

    private final Logger log = LoggerFactory.getLogger(UmuyoboziQueryService.class);

    private final UmuyoboziRepository umuyoboziRepository;

    public UmuyoboziQueryService(UmuyoboziRepository umuyoboziRepository) {
        this.umuyoboziRepository = umuyoboziRepository;
    }

    /**
     * Return a {@link List} of {@link Umuyobozi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Umuyobozi> findByCriteria(UmuyoboziCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Umuyobozi> specification = createSpecification(criteria);
        return umuyoboziRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Umuyobozi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Umuyobozi> findByCriteria(UmuyoboziCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Umuyobozi> specification = createSpecification(criteria);
        return umuyoboziRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UmuyoboziCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Umuyobozi> specification = createSpecification(criteria);
        return umuyoboziRepository.count(specification);
    }

    /**
     * Function to convert {@link UmuyoboziCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Umuyobozi> createSpecification(UmuyoboziCriteria criteria) {
        Specification<Umuyobozi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Umuyobozi_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Umuyobozi_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Umuyobozi_.lastName));
            }
            if (criteria.getPhoneOne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneOne(), Umuyobozi_.phoneOne));
            }
            if (criteria.getPhoneTwo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneTwo(), Umuyobozi_.phoneTwo));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Umuyobozi_.email));
            }
            if (criteria.getUmurimoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUmurimoId(), root -> root.join(Umuyobozi_.umurimo, JoinType.LEFT).get(Umurimo_.id))
                    );
            }
        }
        return specification;
    }
}
