package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Office;
import com.minaloc.gov.repository.OfficeRepository;
import com.minaloc.gov.service.OfficeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Office}.
 */
@Service
@Transactional
public class OfficeServiceImpl implements OfficeService {

    private final Logger log = LoggerFactory.getLogger(OfficeServiceImpl.class);

    private final OfficeRepository officeRepository;

    public OfficeServiceImpl(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    public Office save(Office office) {
        log.debug("Request to save Office : {}", office);
        return officeRepository.save(office);
    }

    @Override
    public Office update(Office office) {
        log.debug("Request to save Office : {}", office);
        return officeRepository.save(office);
    }

    @Override
    public Optional<Office> partialUpdate(Office office) {
        log.debug("Request to partially update Office : {}", office);

        return officeRepository
            .findById(office.getId())
            .map(existingOffice -> {
                if (office.getOfficeType() != null) {
                    existingOffice.setOfficeType(office.getOfficeType());
                }
                if (office.getName() != null) {
                    existingOffice.setName(office.getName());
                }
                if (office.getCreatedAt() != null) {
                    existingOffice.setCreatedAt(office.getCreatedAt());
                }

                return existingOffice;
            })
            .map(officeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Office> findAll() {
        log.debug("Request to get all Offices");
        return officeRepository.findAllWithEagerRelationships();
    }

    public Page<Office> findAllWithEagerRelationships(Pageable pageable) {
        return officeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Office> findOne(Long id) {
        log.debug("Request to get Office : {}", id);
        return officeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Office : {}", id);
        officeRepository.deleteById(id);
    }
}
