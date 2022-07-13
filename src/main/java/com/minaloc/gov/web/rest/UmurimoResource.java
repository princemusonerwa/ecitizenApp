package com.minaloc.gov.web.rest;

import com.minaloc.gov.domain.Umurimo;
import com.minaloc.gov.repository.UmurimoRepository;
import com.minaloc.gov.service.UmurimoQueryService;
import com.minaloc.gov.service.UmurimoService;
import com.minaloc.gov.service.criteria.UmurimoCriteria;
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
 * REST controller for managing {@link com.minaloc.gov.domain.Umurimo}.
 */
@RestController
@RequestMapping("/api")
public class UmurimoResource {

    private final Logger log = LoggerFactory.getLogger(UmurimoResource.class);

    private static final String ENTITY_NAME = "umurimo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UmurimoService umurimoService;

    private final UmurimoRepository umurimoRepository;

    private final UmurimoQueryService umurimoQueryService;

    public UmurimoResource(UmurimoService umurimoService, UmurimoRepository umurimoRepository, UmurimoQueryService umurimoQueryService) {
        this.umurimoService = umurimoService;
        this.umurimoRepository = umurimoRepository;
        this.umurimoQueryService = umurimoQueryService;
    }

    /**
     * {@code POST  /umurimos} : Create a new umurimo.
     *
     * @param umurimo the umurimo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new umurimo, or with status {@code 400 (Bad Request)} if the umurimo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/umurimos")
    public ResponseEntity<Umurimo> createUmurimo(@Valid @RequestBody Umurimo umurimo) throws URISyntaxException {
        log.debug("REST request to save Umurimo : {}", umurimo);
        if (umurimo.getId() != null) {
            throw new BadRequestAlertException("A new umurimo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Umurimo result = umurimoService.save(umurimo);
        return ResponseEntity
            .created(new URI("/api/umurimos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /umurimos/:id} : Updates an existing umurimo.
     *
     * @param id the id of the umurimo to save.
     * @param umurimo the umurimo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated umurimo,
     * or with status {@code 400 (Bad Request)} if the umurimo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the umurimo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/umurimos/{id}")
    public ResponseEntity<Umurimo> updateUmurimo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Umurimo umurimo
    ) throws URISyntaxException {
        log.debug("REST request to update Umurimo : {}, {}", id, umurimo);
        if (umurimo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umurimo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!umurimoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Umurimo result = umurimoService.update(umurimo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, umurimo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /umurimos/:id} : Partial updates given fields of an existing umurimo, field will ignore if it is null
     *
     * @param id the id of the umurimo to save.
     * @param umurimo the umurimo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated umurimo,
     * or with status {@code 400 (Bad Request)} if the umurimo is not valid,
     * or with status {@code 404 (Not Found)} if the umurimo is not found,
     * or with status {@code 500 (Internal Server Error)} if the umurimo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/umurimos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Umurimo> partialUpdateUmurimo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Umurimo umurimo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Umurimo partially : {}, {}", id, umurimo);
        if (umurimo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umurimo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!umurimoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Umurimo> result = umurimoService.partialUpdate(umurimo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, umurimo.getId().toString())
        );
    }

    /**
     * {@code GET  /umurimos} : get all the umurimos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of umurimos in body.
     */
    @GetMapping("/umurimos")
    public ResponseEntity<List<Umurimo>> getAllUmurimos(UmurimoCriteria criteria) {
        log.debug("REST request to get Umurimos by criteria: {}", criteria);
        List<Umurimo> entityList = umurimoQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /umurimos/count} : count all the umurimos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/umurimos/count")
    public ResponseEntity<Long> countUmurimos(UmurimoCriteria criteria) {
        log.debug("REST request to count Umurimos by criteria: {}", criteria);
        return ResponseEntity.ok().body(umurimoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /umurimos/:id} : get the "id" umurimo.
     *
     * @param id the id of the umurimo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the umurimo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/umurimos/{id}")
    public ResponseEntity<Umurimo> getUmurimo(@PathVariable Long id) {
        log.debug("REST request to get Umurimo : {}", id);
        Optional<Umurimo> umurimo = umurimoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(umurimo);
    }

    /**
     * {@code DELETE  /umurimos/:id} : delete the "id" umurimo.
     *
     * @param id the id of the umurimo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/umurimos/{id}")
    public ResponseEntity<Void> deleteUmurimo(@PathVariable Long id) {
        log.debug("REST request to delete Umurimo : {}", id);
        umurimoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
