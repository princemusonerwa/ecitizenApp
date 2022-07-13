package com.minaloc.gov.service;

import com.minaloc.gov.domain.*; // for static metamodels
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.repository.UmuturageRepository;
import com.minaloc.gov.service.criteria.UmuturageCriteria;
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
 * Service for executing complex queries for {@link Umuturage} entities in the database.
 * The main input is a {@link UmuturageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Umuturage} or a {@link Page} of {@link Umuturage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UmuturageQueryService extends QueryService<Umuturage> {

    private final Logger log = LoggerFactory.getLogger(UmuturageQueryService.class);

    private final UmuturageRepository umuturageRepository;

    public UmuturageQueryService(UmuturageRepository umuturageRepository) {
        this.umuturageRepository = umuturageRepository;
    }

    /**
     * Return a {@link List} of {@link Umuturage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Umuturage> findByCriteria(UmuturageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Umuturage> specification = createSpecification(criteria);
        return umuturageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Umuturage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Umuturage> findByCriteria(UmuturageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Umuturage> specification = createSpecification(criteria);
        return umuturageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UmuturageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Umuturage> specification = createSpecification(criteria);
        return umuturageRepository.count(specification);
    }

    /**
     * Function to convert {@link UmuturageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Umuturage> createSpecification(UmuturageCriteria criteria) {
        Specification<Umuturage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Umuturage_.id));
            }
            if (criteria.getIndangamuntu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndangamuntu(), Umuturage_.indangamuntu));
            }
            if (criteria.getAmazina() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAmazina(), Umuturage_.amazina));
            }
            if (criteria.getDob() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDob(), Umuturage_.dob));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Umuturage_.gender));
            }
            if (criteria.getUbudeheCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUbudeheCategory(), Umuturage_.ubudeheCategory));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Umuturage_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Umuturage_.email));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Umuturage_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getVillageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVillageId(), root -> root.join(Umuturage_.village, JoinType.LEFT).get(Village_.id))
                    );
            }
        }
        return specification;
    }
}
