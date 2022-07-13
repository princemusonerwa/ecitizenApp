package com.minaloc.gov.service;

import com.minaloc.gov.domain.*; // for static metamodels
import com.minaloc.gov.domain.Village;
import com.minaloc.gov.repository.VillageRepository;
import com.minaloc.gov.service.criteria.VillageCriteria;
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
 * Service for executing complex queries for {@link Village} entities in the database.
 * The main input is a {@link VillageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Village} or a {@link Page} of {@link Village} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VillageQueryService extends QueryService<Village> {

    private final Logger log = LoggerFactory.getLogger(VillageQueryService.class);

    private final VillageRepository villageRepository;

    public VillageQueryService(VillageRepository villageRepository) {
        this.villageRepository = villageRepository;
    }

    /**
     * Return a {@link List} of {@link Village} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Village> findByCriteria(VillageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Village> specification = createSpecification(criteria);
        return villageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Village} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Village> findByCriteria(VillageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Village> specification = createSpecification(criteria);
        return villageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VillageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Village> specification = createSpecification(criteria);
        return villageRepository.count(specification);
    }

    /**
     * Function to convert {@link VillageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Village> createSpecification(VillageCriteria criteria) {
        Specification<Village> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Village_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Village_.name));
            }
            if (criteria.getUmuturageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUmuturageId(),
                            root -> root.join(Village_.umuturages, JoinType.LEFT).get(Umuturage_.id)
                        )
                    );
            }
            if (criteria.getCellId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCellId(), root -> root.join(Village_.cell, JoinType.LEFT).get(Cell_.id))
                    );
            }
        }
        return specification;
    }
}
