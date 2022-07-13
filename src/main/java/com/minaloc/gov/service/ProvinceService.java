package com.minaloc.gov.service;

import com.minaloc.gov.domain.Province;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Province}.
 */
public interface ProvinceService {
    /**
     * Save a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    Province save(Province province);

    /**
     * Updates a province.
     *
     * @param province the entity to update.
     * @return the persisted entity.
     */
    Province update(Province province);

    /**
     * Partially updates a province.
     *
     * @param province the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Province> partialUpdate(Province province);

    /**
     * Get all the provinces.
     *
     * @return the list of entities.
     */
    List<Province> findAll();

    /**
     * Get the "id" province.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Province> findOne(Long id);

    /**
     * Delete the "id" province.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
