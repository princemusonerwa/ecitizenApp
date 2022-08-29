package com.minaloc.gov.service;

import com.minaloc.gov.domain.Complain;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Complain}.
 */
public interface ComplainService {
    /**
     * Save a complain.
     *
     * @param complain the entity to save.
     * @return the persisted entity.
     */
    Complain save(Complain complain);

    /**
     * Updates a complain.
     *
     * @param complain the entity to update.
     * @return the persisted entity.
     */
    Complain update(Complain complain);

    /**
     * Partially updates a complain.
     *
     * @param complain the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Complain> partialUpdate(Complain complain);

    /**
     * Get all the complains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Complain> findAll(Pageable pageable, String keyword);

    /**
     * Get all the complains with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Complain> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" complain.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Complain> findOne(Long id);

    /**
     * Delete the "id" complain.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
