package com.minaloc.gov.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.domain.User;
import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.repository.UserRepository;
import com.minaloc.gov.service.ComplainQueryService;
import com.minaloc.gov.service.ComplainService;
import com.minaloc.gov.service.criteria.ComplainCriteria;
import com.minaloc.gov.service.dto.OfficeType;
import com.minaloc.gov.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.minaloc.gov.domain.Complain}.
 */
@RestController
@RequestMapping("/api")
public class ComplainResource {

    private final Logger log = LoggerFactory.getLogger(ComplainResource.class);

    private static final String ENTITY_NAME = "complain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComplainService complainService;

    private final ComplainRepository complainRepository;

    private final ComplainQueryService complainQueryService;

    private final UserRepository userRepository;

    public ComplainResource(
        ComplainService complainService,
        ComplainRepository complainRepository,
        ComplainQueryService complainQueryService,
        UserRepository userRepository
    ) {
        this.complainService = complainService;
        this.complainRepository = complainRepository;
        this.complainQueryService = complainQueryService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /complains} : Create a new complain.
     *
     * @param complain the complain to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complain, or with status {@code 400 (Bad Request)} if the complain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/complains")
    public ResponseEntity<Complain> createComplain(@Valid @RequestBody Complain complain) throws URISyntaxException {
        log.debug("REST request to save Complain : {}", complain);
        if (complain.getId() != null) {
            throw new BadRequestAlertException("A new complain cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        User user = userRepository.findByLogin(username);
        complain.setUser(user);
        Complain result = complainService.save(complain);
        return ResponseEntity
            .created(new URI("/api/complains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /complains/:id} : Updates an existing complain.
     *
     * @param id the id of the complain to save.
     * @param complain the complain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complain,
     * or with status {@code 400 (Bad Request)} if the complain is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/complains/{id}")
    public ResponseEntity<Complain> updateComplain(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Complain complain
    ) throws URISyntaxException {
        log.debug("REST request to update Complain : {}, {}", id, complain);
        if (complain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complain.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Complain result = complainService.update(complain);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, complain.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /complains/:id} : Partial updates given fields of an existing complain, field will ignore if it is null
     *
     * @param id the id of the complain to save.
     * @param complain the complain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complain,
     * or with status {@code 400 (Bad Request)} if the complain is not valid,
     * or with status {@code 404 (Not Found)} if the complain is not found,
     * or with status {@code 500 (Internal Server Error)} if the complain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/complains/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Complain> partialUpdateComplain(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Complain complain
    ) throws URISyntaxException {
        log.debug("REST request to partial update Complain partially : {}, {}", id, complain);
        if (complain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complain.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Complain> result = complainService.partialUpdate(complain);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, complain.getId().toString())
        );
    }

    /**
     * {@code GET  /complains} : get all the complains.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of complains in body.
     */
    @GetMapping("/complains")
    public ResponseEntity<List<Complain>> getAllComplains(
        ComplainCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam("officeType") String officeType,
        @RequestParam("officeName") String officeName
    ) {
        System.out.println(officeType);
        System.out.println(officeName);
        Page<Complain> page;
        HttpHeaders headers;
        switch (officeType.toLowerCase()) {
            case "province":
                log.debug("REST request to get Complains by criteria: {}", criteria);
                page = complainQueryService.findAllComplainsByProvince(pageable, officeName);
                headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                return ResponseEntity.ok().headers(headers).body(page.getContent());
            case "district":
                log.debug("REST request to get Complains by criteria: {}", criteria);
                page = complainQueryService.findAllComplainsByDistrict(pageable, officeName);
                headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                return ResponseEntity.ok().headers(headers).body(page.getContent());

            case "sector":
                log.debug("REST request to get Complains by criteria: {}", criteria);
                page = complainQueryService.findAllComplainsBySector(pageable, officeName);
                headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                return ResponseEntity.ok().headers(headers).body(page.getContent());
            case "cell":
                log.debug("REST request to get Complains by criteria: {}", criteria);
                page = complainQueryService.findAllComplainsByCell(pageable, officeName);
                headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                return ResponseEntity.ok().headers(headers).body(page.getContent());
            case "minaloc":
                log.debug("REST request to get Complains by criteria: {}", criteria);
                page = complainQueryService.findByCriteria(criteria, pageable);
                headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
                return ResponseEntity.ok().headers(headers).body(page.getContent());
            default:
                return ResponseEntity.badRequest().body(null);
        }

    }

    /**
     * {@code GET  /complains/count} : count all the complains.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/complains/count")
    public ResponseEntity<Long> countComplains(ComplainCriteria criteria) {
        log.debug("REST request to count Complains by criteria: {}", criteria);
        return ResponseEntity.ok().body(complainQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /complains/:id} : get the "id" complain.
     *
     * @param id the id of the complain to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the complain, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/complains/{id}")
    public ResponseEntity<Complain> getComplain(@PathVariable Long id) {
        log.debug("REST request to get Complain : {}", id);
        Optional<Complain> complain = complainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(complain);
    }

    /**
     * {@code DELETE  /complains/:id} : delete the "id" complain.
     *
     * @param id the id of the complain to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/complains/{id}")
    public ResponseEntity<Void> deleteComplain(@PathVariable Long id) {
        log.debug("REST request to delete Complain : {}", id);
        complainService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
