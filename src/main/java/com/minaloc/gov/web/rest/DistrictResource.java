package com.minaloc.gov.web.rest;

import com.minaloc.gov.domain.District;
import com.minaloc.gov.repository.DistrictRepository;
import com.minaloc.gov.service.DistrictQueryService;
import com.minaloc.gov.service.DistrictService;
import com.minaloc.gov.service.criteria.DistrictCriteria;
import com.minaloc.gov.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.minaloc.gov.domain.District}.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    private static final String ENTITY_NAME = "district";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistrictService districtService;

    private final DistrictRepository districtRepository;

    private final DistrictQueryService districtQueryService;

    public DistrictResource(
        DistrictService districtService,
        DistrictRepository districtRepository,
        DistrictQueryService districtQueryService
    ) {
        this.districtService = districtService;
        this.districtRepository = districtRepository;
        this.districtQueryService = districtQueryService;
    }

    /**
     * {@code POST  /districts} : Create a new district.
     *
     * @param district the district to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new district, or with status {@code 400 (Bad Request)} if the district has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/districts")
    public ResponseEntity<District> createDistrict(@Valid @RequestBody District district) throws URISyntaxException {
        log.debug("REST request to save District : {}", district);
        if (district.getId() != null) {
            throw new BadRequestAlertException("A new district cannot already have an ID", ENTITY_NAME, "idexists");
        }
        District result = districtService.save(district);
        return ResponseEntity
            .created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /districts/:id} : Updates an existing district.
     *
     * @param id the id of the district to save.
     * @param district the district to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated district,
     * or with status {@code 400 (Bad Request)} if the district is not valid,
     * or with status {@code 500 (Internal Server Error)} if the district couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/districts/{id}")
    public ResponseEntity<District> updateDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody District district
    ) throws URISyntaxException {
        log.debug("REST request to update District : {}, {}", id, district);
        if (district.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, district.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!districtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        District result = districtService.update(district);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, district.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /districts/:id} : Partial updates given fields of an existing district, field will ignore if it is null
     *
     * @param id the id of the district to save.
     * @param district the district to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated district,
     * or with status {@code 400 (Bad Request)} if the district is not valid,
     * or with status {@code 404 (Not Found)} if the district is not found,
     * or with status {@code 500 (Internal Server Error)} if the district couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/districts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<District> partialUpdateDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody District district
    ) throws URISyntaxException {
        log.debug("REST request to partial update District partially : {}, {}", id, district);
        if (district.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, district.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!districtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<District> result = districtService.partialUpdate(district);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, district.getId().toString())
        );
    }

    /**
     * {@code GET  /districts} : get all the districts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of districts in body.
     */
    @GetMapping("/districts")
    public ResponseEntity<List<District>> getAllDistricts(DistrictCriteria criteria) {
        log.debug("REST request to get Districts by criteria: {}", criteria);
        List<District> entityList = districtQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /districts/count} : count all the districts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/districts/count")
    public ResponseEntity<Long> countDistricts(DistrictCriteria criteria) {
        log.debug("REST request to count Districts by criteria: {}", criteria);
        return ResponseEntity.ok().body(districtQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /districts/:id} : get the "id" district.
     *
     * @param id the id of the district to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the district, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/districts/{id}")
    public ResponseEntity<District> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        Optional<District> district = districtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(district);
    }

    /**
     * {@code DELETE  /districts/:id} : delete the "id" district.
     *
     * @param id the id of the district to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/districts/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        districtService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
