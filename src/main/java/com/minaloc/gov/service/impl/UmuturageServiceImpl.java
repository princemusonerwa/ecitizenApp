package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.repository.UmuturageRepository;
import com.minaloc.gov.service.UmuturageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Umuturage}.
 */
@Service
@Transactional
public class UmuturageServiceImpl implements UmuturageService {

    private final Logger log = LoggerFactory.getLogger(UmuturageServiceImpl.class);

    private final UmuturageRepository umuturageRepository;

    public UmuturageServiceImpl(UmuturageRepository umuturageRepository) {
        this.umuturageRepository = umuturageRepository;
    }

    @Override
    public Umuturage save(Umuturage umuturage) {
        log.debug("Request to save Umuturage : {}", umuturage);
        return umuturageRepository.save(umuturage);
    }

    @Override
    public Umuturage update(Umuturage umuturage) {
        log.debug("Request to save Umuturage : {}", umuturage);
        return umuturageRepository.save(umuturage);
    }

    @Override
    public Optional<Umuturage> partialUpdate(Umuturage umuturage) {
        log.debug("Request to partially update Umuturage : {}", umuturage);

        return umuturageRepository
            .findById(umuturage.getId())
            .map(existingUmuturage -> {
                if (umuturage.getIndangamuntu() != null) {
                    existingUmuturage.setIndangamuntu(umuturage.getIndangamuntu());
                }
                if (umuturage.getAmazina() != null) {
                    existingUmuturage.setAmazina(umuturage.getAmazina());
                }
                if (umuturage.getDob() != null) {
                    existingUmuturage.setDob(umuturage.getDob());
                }
                if (umuturage.getGender() != null) {
                    existingUmuturage.setGender(umuturage.getGender());
                }
                if (umuturage.getUbudeheCategory() != null) {
                    existingUmuturage.setUbudeheCategory(umuturage.getUbudeheCategory());
                }
                if (umuturage.getPhone() != null) {
                    existingUmuturage.setPhone(umuturage.getPhone());
                }
                if (umuturage.getEmail() != null) {
                    existingUmuturage.setEmail(umuturage.getEmail());
                }

                return existingUmuturage;
            })
            .map(umuturageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Umuturage> findAll(Pageable pageable) {
        log.debug("Request to get all Umuturages");
        return umuturageRepository.findAll(pageable);
    }

    public Page<Umuturage> findAllWithEagerRelationships(Pageable pageable) {
        return umuturageRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Umuturage> findOne(Long id) {
        log.debug("Request to get Umuturage : {}", id);
        return umuturageRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Umuturage : {}", id);
        umuturageRepository.deleteById(id);
    }
}
