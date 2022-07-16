package com.minaloc.gov.service.impl;

import com.minaloc.gov.domain.Province;
import com.minaloc.gov.repository.ProvinceRepository;
import com.minaloc.gov.service.ProvinceService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    @Override
    public Province save(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    @Override
    public Province update(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    @Override
    public Optional<Province> partialUpdate(Province province) {
        log.debug("Request to partially update Province : {}", province);

        return provinceRepository
            .findById(province.getId())
            .map(existingProvince -> {
                if (province.getProvinceCode() != null) {
                    existingProvince.setProvinceCode(province.getProvinceCode());
                }
                if (province.getName() != null) {
                    existingProvince.setName(province.getName());
                }

                return existingProvince;
            })
            .map(provinceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Province> findAll() {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Province> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}
