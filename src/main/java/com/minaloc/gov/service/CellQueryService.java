package com.minaloc.gov.service;

import com.minaloc.gov.domain.*; // for static metamodels
import com.minaloc.gov.domain.Cell;
import com.minaloc.gov.repository.CellRepository;
import com.minaloc.gov.service.criteria.CellCriteria;
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
 * Service for executing complex queries for {@link Cell} entities in the database.
 * The main input is a {@link CellCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cell} or a {@link Page} of {@link Cell} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CellQueryService extends QueryService<Cell> {

    private final Logger log = LoggerFactory.getLogger(CellQueryService.class);

    private final CellRepository cellRepository;

    public CellQueryService(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    /**
     * Return a {@link List} of {@link Cell} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cell> findByCriteria(CellCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cell> specification = createSpecification(criteria);
        return cellRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cell} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cell> findByCriteria(CellCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cell> specification = createSpecification(criteria);
        return cellRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CellCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cell> specification = createSpecification(criteria);
        return cellRepository.count(specification);
    }

    /**
     * Function to convert {@link CellCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cell> createSpecification(CellCriteria criteria) {
        Specification<Cell> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cell_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cell_.name));
            }
            if (criteria.getVillageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVillageId(), root -> root.join(Cell_.villages, JoinType.LEFT).get(Village_.id))
                    );
            }
            if (criteria.getSectorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSectorId(), root -> root.join(Cell_.sector, JoinType.LEFT).get(Sector_.id))
                    );
            }
        }
        return specification;
    }
}
