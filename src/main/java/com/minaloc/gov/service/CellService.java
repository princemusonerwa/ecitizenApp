package com.minaloc.gov.service;

import com.minaloc.gov.domain.Cell;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cell}.
 */
public interface CellService {
    /**
     * Save a cell.
     *
     * @param cell the entity to save.
     * @return the persisted entity.
     */
    Cell save(Cell cell);

    /**
     * Updates a cell.
     *
     * @param cell the entity to update.
     * @return the persisted entity.
     */
    Cell update(Cell cell);

    /**
     * Partially updates a cell.
     *
     * @param cell the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cell> partialUpdate(Cell cell);

    /**
     * Get all the cells.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cell> findAll(Pageable pageable);

    /**
     * Get all the cells with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cell> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cell.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cell> findOne(Long id);

    /**
     * Delete the "id" cell.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
