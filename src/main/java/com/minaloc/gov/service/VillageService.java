package com.minaloc.gov.service;

import com.minaloc.gov.domain.Village;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Village}.
 */
public interface VillageService {
    /**
     * Save a village.
     *
     * @param village the entity to save.
     * @return the persisted entity.
     */
    Village save(Village village);

    /**
     * Updates a village.
     *
     * @param village the entity to update.
     * @return the persisted entity.
     */
    Village update(Village village);

    /**
     * Partially updates a village.
     *
     * @param village the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Village> partialUpdate(Village village);

    /**
     * Get all the villages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Village> findAll(Pageable pageable);

    /**
     * Get all the villages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Village> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" village.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Village> findOne(Long id);

    /**
     * Delete the "id" village.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
