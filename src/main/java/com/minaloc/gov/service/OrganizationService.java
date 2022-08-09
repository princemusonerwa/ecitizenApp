package com.minaloc.gov.service;

import com.minaloc.gov.domain.Organization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Organization}.
 */
public interface OrganizationService {
    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    Organization save(Organization organization);

    /**
     * Updates a organization.
     *
     * @param organization the entity to update.
     * @return the persisted entity.
     */
    Organization update(Organization organization);

    /**
     * Partially updates a organization.
     *
     * @param organization the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Organization> partialUpdate(Organization organization);

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    List<Organization> findAll();

    /**
     * Get all the organizations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Organization> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Organization> findOne(Long id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
