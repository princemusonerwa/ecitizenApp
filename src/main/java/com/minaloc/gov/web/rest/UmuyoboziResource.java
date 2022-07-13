package com.minaloc.gov.web.rest;

import com.minaloc.gov.domain.Umuyobozi;
import com.minaloc.gov.repository.UmuyoboziRepository;
import com.minaloc.gov.service.UmuyoboziQueryService;
import com.minaloc.gov.service.UmuyoboziService;
import com.minaloc.gov.service.criteria.UmuyoboziCriteria;
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
 * REST controller for managing {@link com.minaloc.gov.domain.Umuyobozi}.
 */
@RestController
@RequestMapping("/api")
public class UmuyoboziResource {

    private final Logger log = LoggerFactory.getLogger(UmuyoboziResource.class);

    private static final String ENTITY_NAME = "umuyobozi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UmuyoboziService umuyoboziService;

    private final UmuyoboziRepository umuyoboziRepository;

    private final UmuyoboziQueryService umuyoboziQueryService;

    public UmuyoboziResource(
        UmuyoboziService umuyoboziService,
        UmuyoboziRepository umuyoboziRepository,
        UmuyoboziQueryService umuyoboziQueryService
    ) {
        this.umuyoboziService = umuyoboziService;
        this.umuyoboziRepository = umuyoboziRepository;
        this.umuyoboziQueryService = umuyoboziQueryService;
    }

    /**
     * {@code POST  /umuyobozis} : Create a new umuyobozi.
     *
     * @param umuyobozi the umuyobozi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new umuyobozi, or with status {@code 400 (Bad Request)} if the umuyobozi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/umuyobozis")
    public ResponseEntity<Umuyobozi> createUmuyobozi(@Valid @RequestBody Umuyobozi umuyobozi) throws URISyntaxException {
        log.debug("REST request to save Umuyobozi : {}", umuyobozi);
        if (umuyobozi.getId() != null) {
            throw new BadRequestAlertException("A new umuyobozi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Umuyobozi result = umuyoboziService.save(umuyobozi);
        return ResponseEntity
            .created(new URI("/api/umuyobozis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /umuyobozis/:id} : Updates an existing umuyobozi.
     *
     * @param id the id of the umuyobozi to save.
     * @param umuyobozi the umuyobozi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated umuyobozi,
     * or with status {@code 400 (Bad Request)} if the umuyobozi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the umuyobozi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/umuyobozis/{id}")
    public ResponseEntity<Umuyobozi> updateUmuyobozi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Umuyobozi umuyobozi
    ) throws URISyntaxException {
        log.debug("REST request to update Umuyobozi : {}, {}", id, umuyobozi);
        if (umuyobozi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umuyobozi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!umuyoboziRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Umuyobozi result = umuyoboziService.update(umuyobozi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, umuyobozi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /umuyobozis/:id} : Partial updates given fields of an existing umuyobozi, field will ignore if it is null
     *
     * @param id the id of the umuyobozi to save.
     * @param umuyobozi the umuyobozi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated umuyobozi,
     * or with status {@code 400 (Bad Request)} if the umuyobozi is not valid,
     * or with status {@code 404 (Not Found)} if the umuyobozi is not found,
     * or with status {@code 500 (Internal Server Error)} if the umuyobozi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/umuyobozis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Umuyobozi> partialUpdateUmuyobozi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Umuyobozi umuyobozi
    ) throws URISyntaxException {
        log.debug("REST request to partial update Umuyobozi partially : {}, {}", id, umuyobozi);
        if (umuyobozi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umuyobozi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!umuyoboziRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Umuyobozi> result = umuyoboziService.partialUpdate(umuyobozi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, umuyobozi.getId().toString())
        );
    }

    /**
     * {@code GET  /umuyobozis} : get all the umuyobozis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of umuyobozis in body.
     */
    @GetMapping("/umuyobozis")
    public ResponseEntity<List<Umuyobozi>> getAllUmuyobozis(
        UmuyoboziCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Umuyobozis by criteria: {}", criteria);
        Page<Umuyobozi> page = umuyoboziQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /umuyobozis/count} : count all the umuyobozis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/umuyobozis/count")
    public ResponseEntity<Long> countUmuyobozis(UmuyoboziCriteria criteria) {
        log.debug("REST request to count Umuyobozis by criteria: {}", criteria);
        return ResponseEntity.ok().body(umuyoboziQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /umuyobozis/:id} : get the "id" umuyobozi.
     *
     * @param id the id of the umuyobozi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the umuyobozi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/umuyobozis/{id}")
    public ResponseEntity<Umuyobozi> getUmuyobozi(@PathVariable Long id) {
        log.debug("REST request to get Umuyobozi : {}", id);
        Optional<Umuyobozi> umuyobozi = umuyoboziService.findOne(id);
        return ResponseUtil.wrapOrNotFound(umuyobozi);
    }

    /**
     * {@code DELETE  /umuyobozis/:id} : delete the "id" umuyobozi.
     *
     * @param id the id of the umuyobozi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/umuyobozis/{id}")
    public ResponseEntity<Void> deleteUmuyobozi(@PathVariable Long id) {
        log.debug("REST request to delete Umuyobozi : {}", id);
        umuyoboziService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
