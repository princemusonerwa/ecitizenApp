package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Umurimo;
import com.minaloc.gov.repository.UmurimoRepository;
import com.minaloc.gov.service.UmurimoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Umurimo}.
 */
@Service
@Transactional
public class UmurimoServiceImpl implements UmurimoService {

    private final Logger log = LoggerFactory.getLogger(UmurimoServiceImpl.class);

    private final UmurimoRepository umurimoRepository;

    public UmurimoServiceImpl(UmurimoRepository umurimoRepository) {
        this.umurimoRepository = umurimoRepository;
    }

    @Override
    public Umurimo save(Umurimo umurimo) {
        log.debug("Request to save Umurimo : {}", umurimo);
        return umurimoRepository.save(umurimo);
    }

    @Override
    public Umurimo update(Umurimo umurimo) {
        log.debug("Request to save Umurimo : {}", umurimo);
        return umurimoRepository.save(umurimo);
    }

    @Override
    public Optional<Umurimo> partialUpdate(Umurimo umurimo) {
        log.debug("Request to partially update Umurimo : {}", umurimo);

        return umurimoRepository
            .findById(umurimo.getId())
            .map(existingUmurimo -> {
                if (umurimo.getUmurimo() != null) {
                    existingUmurimo.setUmurimo(umurimo.getUmurimo());
                }
                if (umurimo.getOfficeType() != null) {
                    existingUmurimo.setOfficeType(umurimo.getOfficeType());
                }

                return existingUmurimo;
            })
            .map(umurimoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Umurimo> findAll() {
        log.debug("Request to get all Umurimos");
        return umurimoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Umurimo> findOne(Long id) {
        log.debug("Request to get Umurimo : {}", id);
        return umurimoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Umurimo : {}", id);
        umurimoRepository.deleteById(id);
    }
}
