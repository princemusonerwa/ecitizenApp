package com.minaloc.gov.service;

import com.minaloc.gov.domain.*; // for static metamodels
import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.service.criteria.ComplainCriteria;
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
 * Service for executing complex queries for {@link Complain} entities in the database.
 * The main input is a {@link ComplainCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Complain} or a {@link Page} of {@link Complain} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComplainQueryService extends QueryService<Complain> {

    private final Logger log = LoggerFactory.getLogger(ComplainQueryService.class);

    private final ComplainRepository complainRepository;

    public ComplainQueryService(ComplainRepository complainRepository) {
        this.complainRepository = complainRepository;
    }

    /**
     * Return a {@link List} of {@link Complain} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Complain> findByCriteria(ComplainCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Complain> specification = createSpecification(criteria);
        return complainRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Complain} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Complain> findByCriteria(ComplainCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Complain> specification = createSpecification(criteria);
        return complainRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComplainCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Complain> specification = createSpecification(criteria);
        return complainRepository.count(specification);
    }

    /**
     * Function to convert {@link ComplainCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Complain> createSpecification(ComplainCriteria criteria) {
        Specification<Complain> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Complain_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Complain_.date));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Complain_.status));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), Complain_.priority));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoryId(), root -> root.join(Complain_.category, JoinType.LEFT).get(Category_.id))
                    );
            }
            if (criteria.getUmuturageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUmuturageId(),
                            root -> root.join(Complain_.umuturage, JoinType.LEFT).get(Umuturage_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Complain_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(Complain_.organizations, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
