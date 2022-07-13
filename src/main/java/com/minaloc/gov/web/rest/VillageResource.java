package com.minaloc.gov.web.rest;

import com.minaloc.gov.domain.Village;
import com.minaloc.gov.repository.VillageRepository;
import com.minaloc.gov.service.VillageQueryService;
import com.minaloc.gov.service.VillageService;
import com.minaloc.gov.service.criteria.VillageCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.minaloc.gov.domain.Village}.
 */
@RestController
@RequestMapping("/api")
public class VillageResource {

    private final Logger log = LoggerFactory.getLogger(VillageResource.class);

    private static final String ENTITY_NAME = "village";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VillageService villageService;

    private final VillageRepository villageRepository;

    private final VillageQueryService villageQueryService;

    public VillageResource(VillageService villageService, VillageRepository villageRepository, VillageQueryService villageQueryService) {
        this.villageService = villageService;
        this.villageRepository = villageRepository;
        this.villageQueryService = villageQueryService;
    }

    /**
     * {@code POST  /villages} : Create a new village.
     *
     * @param village the village to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new village, or with status {@code 400 (Bad Request)} if the village has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/villages")
    public ResponseEntity<Village> createVillage(@Valid @RequestBody Village village) throws URISyntaxException {
        log.debug("REST request to save Village : {}", village);
        if (village.getId() != null) {
            throw new BadRequestAlertException("A new village cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Village result = villageService.save(village);
        return ResponseEntity
            .created(new URI("/api/villages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /villages/:id} : Updates an existing village.
     *
     * @param id the id of the village to save.
     * @param village the village to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated village,
     * or with status {@code 400 (Bad Request)} if the village is not valid,
     * or with status {@code 500 (Internal Server Error)} if the village couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/villages/{id}")
    public ResponseEntity<Village> updateVillage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Village village
    ) throws URISyntaxException {
        log.debug("REST request to update Village : {}, {}", id, village);
        if (village.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, village.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!villageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Village result = villageService.update(village);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, village.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /villages/:id} : Partial updates given fields of an existing village, field will ignore if it is null
     *
     * @param id the id of the village to save.
     * @param village the village to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated village,
     * or with status {@code 400 (Bad Request)} if the village is not valid,
     * or with status {@code 404 (Not Found)} if the village is not found,
     * or with status {@code 500 (Internal Server Error)} if the village couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/villages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Village> partialUpdateVillage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Village village
    ) throws URISyntaxException {
        log.debug("REST request to partial update Village partially : {}, {}", id, village);
        if (village.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, village.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!villageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Village> result = villageService.partialUpdate(village);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, village.getId().toString())
        );
    }

    /**
     * {@code GET  /villages} : get all the villages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of villages in body.
     */
    @GetMapping("/villages")
    public ResponseEntity<List<Village>> getAllVillages(
        VillageCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Villages by criteria: {}", criteria);
        Page<Village> page = villageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /villages/count} : count all the villages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/villages/count")
    public ResponseEntity<Long> countVillages(VillageCriteria criteria) {
        log.debug("REST request to count Villages by criteria: {}", criteria);
        return ResponseEntity.ok().body(villageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /villages/:id} : get the "id" village.
     *
     * @param id the id of the village to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the village, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/villages/{id}")
    public ResponseEntity<Village> getVillage(@PathVariable Long id) {
        log.debug("REST request to get Village : {}", id);
        Optional<Village> village = villageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(village);
    }

    /**
     * {@code DELETE  /villages/:id} : delete the "id" village.
     *
     * @param id the id of the village to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/villages/{id}")
    public ResponseEntity<Void> deleteVillage(@PathVariable Long id) {
        log.debug("REST request to delete Village : {}", id);
        villageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
