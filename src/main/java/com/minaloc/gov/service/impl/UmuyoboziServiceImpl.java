package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Umuyobozi;
import com.minaloc.gov.repository.UmuyoboziRepository;
import com.minaloc.gov.service.UmuyoboziService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Umuyobozi}.
 */
@Service
@Transactional
public class UmuyoboziServiceImpl implements UmuyoboziService {

    private final Logger log = LoggerFactory.getLogger(UmuyoboziServiceImpl.class);

    private final UmuyoboziRepository umuyoboziRepository;

    public UmuyoboziServiceImpl(UmuyoboziRepository umuyoboziRepository) {
        this.umuyoboziRepository = umuyoboziRepository;
    }

    @Override
    public Umuyobozi save(Umuyobozi umuyobozi) {
        log.debug("Request to save Umuyobozi : {}", umuyobozi);
        return umuyoboziRepository.save(umuyobozi);
    }

    @Override
    public Umuyobozi update(Umuyobozi umuyobozi) {
        log.debug("Request to save Umuyobozi : {}", umuyobozi);
        return umuyoboziRepository.save(umuyobozi);
    }

    @Override
    public Optional<Umuyobozi> partialUpdate(Umuyobozi umuyobozi) {
        log.debug("Request to partially update Umuyobozi : {}", umuyobozi);

        return umuyoboziRepository
            .findById(umuyobozi.getId())
            .map(existingUmuyobozi -> {
                if (umuyobozi.getFirstName() != null) {
                    existingUmuyobozi.setFirstName(umuyobozi.getFirstName());
                }
                if (umuyobozi.getLastName() != null) {
                    existingUmuyobozi.setLastName(umuyobozi.getLastName());
                }
                if (umuyobozi.getPhoneOne() != null) {
                    existingUmuyobozi.setPhoneOne(umuyobozi.getPhoneOne());
                }
                if (umuyobozi.getPhoneTwo() != null) {
                    existingUmuyobozi.setPhoneTwo(umuyobozi.getPhoneTwo());
                }
                if (umuyobozi.getEmail() != null) {
                    existingUmuyobozi.setEmail(umuyobozi.getEmail());
                }

                return existingUmuyobozi;
            })
            .map(umuyoboziRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Umuyobozi> findAll(Pageable pageable) {
        log.debug("Request to get all Umuyobozis");
        return umuyoboziRepository.findAll(pageable);
    }

    public Page<Umuyobozi> findAllWithEagerRelationships(Pageable pageable) {
        return umuyoboziRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Umuyobozi> findOne(Long id) {
        log.debug("Request to get Umuyobozi : {}", id);
        return umuyoboziRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Umuyobozi : {}", id);
        umuyoboziRepository.deleteById(id);
    }
}
