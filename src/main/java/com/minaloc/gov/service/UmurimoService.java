package com.minaloc.gov.service;

import com.minaloc.gov.domain.Umurimo;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Umurimo}.
 */
public interface UmurimoService {
    /**
     * Save a umurimo.
     *
     * @param umurimo the entity to save.
     * @return the persisted entity.
     */
    Umurimo save(Umurimo umurimo);

    /**
     * Updates a umurimo.
     *
     * @param umurimo the entity to update.
     * @return the persisted entity.
     */
    Umurimo update(Umurimo umurimo);

    /**
     * Partially updates a umurimo.
     *
     * @param umurimo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Umurimo> partialUpdate(Umurimo umurimo);

    /**
     * Get all the umurimos.
     *
     * @return the list of entities.
     */
    List<Umurimo> findAll();

    /**
     * Get the "id" umurimo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Umurimo> findOne(Long id);

    /**
     * Delete the "id" umurimo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
