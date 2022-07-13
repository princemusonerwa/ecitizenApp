package com.minaloc.gov.service;

import com.minaloc.gov.domain.Umuyobozi;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Umuyobozi}.
 */
public interface UmuyoboziService {
    /**
     * Save a umuyobozi.
     *
     * @param umuyobozi the entity to save.
     * @return the persisted entity.
     */
    Umuyobozi save(Umuyobozi umuyobozi);

    /**
     * Updates a umuyobozi.
     *
     * @param umuyobozi the entity to update.
     * @return the persisted entity.
     */
    Umuyobozi update(Umuyobozi umuyobozi);

    /**
     * Partially updates a umuyobozi.
     *
     * @param umuyobozi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Umuyobozi> partialUpdate(Umuyobozi umuyobozi);

    /**
     * Get all the umuyobozis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Umuyobozi> findAll(Pageable pageable);

    /**
     * Get all the umuyobozis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Umuyobozi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" umuyobozi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Umuyobozi> findOne(Long id);

    /**
     * Delete the "id" umuyobozi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
