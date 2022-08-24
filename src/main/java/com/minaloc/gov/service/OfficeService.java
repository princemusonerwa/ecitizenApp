package com.minaloc.gov.service;

import com.minaloc.gov.domain.Office;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Office}.
 */
public interface OfficeService {
    /**
     * Save a office.
     *
     * @param office the entity to save.
     * @return the persisted entity.
     */
    Office save(Office office);

    /**
     * Updates a office.
     *
     * @param office the entity to update.
     * @return the persisted entity.
     */
    Office update(Office office);

    /**
     * Partially updates a office.
     *
     * @param office the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Office> partialUpdate(Office office);

    /**
     * Get all the offices.
     *
     * @return the list of entities.
     */
    List<Office> findAll();

    /**
     * Get the "id" office.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Office> findOne(Long id);

    /**
     * Delete the "id" office.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
