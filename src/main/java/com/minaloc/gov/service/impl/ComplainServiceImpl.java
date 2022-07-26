package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.service.ComplainService;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Complain}.
 */
@Service
@Transactional
public class ComplainServiceImpl implements ComplainService {

    private final Logger log = LoggerFactory.getLogger(ComplainServiceImpl.class);

    private final ComplainRepository complainRepository;

    public ComplainServiceImpl(ComplainRepository complainRepository) {
        this.complainRepository = complainRepository;
    }

    @Override
    public Complain save(Complain complain) {
        log.debug("Request to save Complain : {}", complain);
        return complainRepository.save(complain);
    }

    @Override
    public Complain update(Complain complain) {
        log.debug("Request to save Complain : {}", complain);
        return complainRepository.save(complain);
    }

    @Override
    public Optional<Complain> partialUpdate(Complain complain) {
        log.debug("Request to partially update Complain : {}", complain);

        return complainRepository
            .findById(complain.getId())
            .map(existingComplain -> {
                if (complain.getIkibazo() != null) {
                    existingComplain.setIkibazo(complain.getIkibazo());
                }
                if (complain.getIcyakozwe() != null) {
                    existingComplain.setIcyakozwe(complain.getIcyakozwe());
                }
                if (complain.getIcyakorwa() != null) {
                    existingComplain.setIcyakorwa(complain.getIcyakorwa());
                }
                if (complain.getUmwanzuro() != null) {
                    existingComplain.setUmwanzuro(complain.getUmwanzuro());
                }
                if (complain.getDate() != null) {
                    existingComplain.setDate(complain.getDate());
                }
                if (complain.getStatus() != null) {
                    existingComplain.setStatus(complain.getStatus());
                }
                if (complain.getPriority() != null) {
                    existingComplain.setPriority(complain.getPriority());
                }

                return existingComplain;
            })
            .map(complainRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Complain> findAll(Pageable pageable) {
        log.debug("Request to get all Complains");
        return complainRepository.findAll(pageable);
    }

    public Page<Complain> findAllWithEagerRelationships(Pageable pageable) {
        return complainRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Complain> findOne(Long id) {
        log.debug("Request to get Complain : {}", id);
        return complainRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Complain : {}", id);
        complainRepository.deleteById(id);
    }
}
