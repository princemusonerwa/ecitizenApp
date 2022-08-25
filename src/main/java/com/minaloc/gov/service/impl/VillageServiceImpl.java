package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Village;
import com.minaloc.gov.repository.VillageRepository;
import com.minaloc.gov.service.VillageService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Village}.
 */
@Service
@Transactional
public class VillageServiceImpl implements VillageService {

    private final Logger log = LoggerFactory.getLogger(VillageServiceImpl.class);

    private final VillageRepository villageRepository;

    public VillageServiceImpl(VillageRepository villageRepository) {
        this.villageRepository = villageRepository;
    }

    @Override
    public Village save(Village village) {
        log.debug("Request to save Village : {}", village);
        return villageRepository.save(village);
    }

    @Override
    public Village update(Village village) {
        log.debug("Request to save Village : {}", village);
        return villageRepository.save(village);
    }

    @Override
    public Optional<Village> partialUpdate(Village village) {
        log.debug("Request to partially update Village : {}", village);

        return villageRepository
            .findById(village.getId())
            .map(existingVillage -> {
                if (village.getVillageCode() != null) {
                    existingVillage.setVillageCode(village.getVillageCode());
                }
                if (village.getName() != null) {
                    existingVillage.setName(village.getName());
                }

                return existingVillage;
            })
            .map(villageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Village> findAll(Pageable pageable) {
        log.debug("Request to get all Villages");
        return villageRepository.findAll(pageable);
    }

    public Page<Village> findAllWithEagerRelationships(Pageable pageable) {
        return villageRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Village> findOne(Long id) {
        log.debug("Request to get Village : {}", id);
        return villageRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Village : {}", id);
        villageRepository.deleteById(id);
    }
}
