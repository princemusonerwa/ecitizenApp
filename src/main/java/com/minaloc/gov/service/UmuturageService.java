package com.minaloc.gov.service;

import com.minaloc.gov.domain.Umuturage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Umuturage}.
 */
public interface UmuturageService {
    /**
     * Save a umuturage.
     *
     * @param umuturage the entity to save.
     * @return the persisted entity.
     */
    Umuturage save(Umuturage umuturage);

    /**
     * Updates a umuturage.
     *
     * @param umuturage the entity to update.
     * @return the persisted entity.
     */
    Umuturage update(Umuturage umuturage);

    /**
     * Partially updates a umuturage.
     *
     * @param umuturage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Umuturage> partialUpdate(Umuturage umuturage);

    /**
     * Get all the umuturages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Umuturage> findAll(Pageable pageable);

    /**
     * Get all the umuturages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Umuturage> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" umuturage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Umuturage> findOne(Long id);

    /**
     * Delete the "id" umuturage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Object getByIdentityCard(String identityCard);

    Optional<Umuturage> findByIndangamuntu(String indangamuntu);
}
