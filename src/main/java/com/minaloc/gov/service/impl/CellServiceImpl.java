package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Cell;
import com.minaloc.gov.repository.CellRepository;
import com.minaloc.gov.service.CellService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cell}.
 */
@Service
@Transactional
public class CellServiceImpl implements CellService {

    private final Logger log = LoggerFactory.getLogger(CellServiceImpl.class);

    private final CellRepository cellRepository;

    public CellServiceImpl(CellRepository cellRepository) {
        this.cellRepository = cellRepository;
    }

    @Override
    public Cell save(Cell cell) {
        log.debug("Request to save Cell : {}", cell);
        return cellRepository.save(cell);
    }

    @Override
    public Cell update(Cell cell) {
        log.debug("Request to save Cell : {}", cell);
        return cellRepository.save(cell);
    }

    @Override
    public Optional<Cell> partialUpdate(Cell cell) {
        log.debug("Request to partially update Cell : {}", cell);

        return cellRepository
            .findById(cell.getId())
            .map(existingCell -> {
                if (cell.getSectorCode() != null) {
                    existingCell.setSectorCode(cell.getSectorCode());
                }
                if (cell.getName() != null) {
                    existingCell.setName(cell.getName());
                }

                return existingCell;
            })
            .map(cellRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cell> findAll(Pageable pageable) {
        log.debug("Request to get all Cells");
        return cellRepository.findAll(pageable);
    }

    public Page<Cell> findAllWithEagerRelationships(Pageable pageable) {
        return cellRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cell> findOne(Long id) {
        log.debug("Request to get Cell : {}", id);
        return cellRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cell : {}", id);
        cellRepository.deleteById(id);
    }
}
