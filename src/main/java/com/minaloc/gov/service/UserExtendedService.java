package com.minaloc.gov.service;

import com.minaloc.gov.domain.UserExtended;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link UserExtended}.
 */
public interface UserExtendedService {
    /**
     * Save a userExtended.
     *
     * @param userExtended the entity to save.
     * @return the persisted entity.
     */
    UserExtended save(UserExtended userExtended);

    /**
     * Updates a userExtended.
     *
     * @param userExtended the entity to update.
     * @return the persisted entity.
     */
    UserExtended update(UserExtended userExtended);

    /**
     * Partially updates a userExtended.
     *
     * @param userExtended the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserExtended> partialUpdate(UserExtended userExtended);

    /**
     * Get all the userExtendeds.
     *
     * @return the list of entities.
     */
    List<UserExtended> findAll();

    /**
     * Get all the userExtendeds with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserExtended> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userExtended.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserExtended> findOne(Long id);

    /**
     * Delete the "id" userExtended.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
