package com.minaloc.gov.web.rest;

import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.domain.User;
import com.minaloc.gov.repository.UmuturageRepository;
import com.minaloc.gov.repository.UserRepository;
import com.minaloc.gov.service.EmailAlreadyUsedException;
import com.minaloc.gov.service.UmuturageQueryService;
import com.minaloc.gov.service.UmuturageService;
import com.minaloc.gov.service.criteria.UmuturageCriteria;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.minaloc.gov.domain.Umuturage}.
 */
@RestController
@RequestMapping("/api")
public class UmuturageResource {

    private final Logger log = LoggerFactory.getLogger(UmuturageResource.class);

    private static final String ENTITY_NAME = "umuturage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UmuturageService umuturageService;

    private final UmuturageRepository umuturageRepository;

    private final UmuturageQueryService umuturageQueryService;

    private final UserRepository userRepository;

    public UmuturageResource(
        UmuturageService umuturageService,
        UmuturageRepository umuturageRepository,
        UmuturageQueryService umuturageQueryService,
        UserRepository userRepository
    ) {
        this.umuturageService = umuturageService;
        this.umuturageRepository = umuturageRepository;
        this.umuturageQueryService = umuturageQueryService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /umuturages} : Create a new umuturage.
     *
     * @param umuturage the umuturage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new umuturage, or with status {@code 400 (Bad Request)} if the umuturage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/umuturages")
    public ResponseEntity<Umuturage> createUmuturage(@Valid @RequestBody Umuturage umuturage) throws URISyntaxException {
        log.debug("REST request to save Umuturage : {}", umuturage);
        if (umuturage.getId() != null) {
            throw new BadRequestAlertException("A new umuturage cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Umuturage> existingUmuturage = umuturageRepository.findByIndangamuntu(umuturage.getIndangamuntu());

        Optional<Umuturage> existEmail = umuturageRepository.findOneByEmailIgnoreCase(umuturage.getEmail());

        if (existingUmuturage.isPresent() && (existingUmuturage.get().getIndangamuntu().equalsIgnoreCase(umuturage.getIndangamuntu()))) {
            throw new BadRequestAlertException("Umuturage with the given indangamuntu already exists", ENTITY_NAME, "indangamuntuexists");
        }

        if (existEmail.isPresent() && (existEmail.get().getEmail().equalsIgnoreCase(umuturage.getEmail()))) {
            throw new BadRequestAlertException("Umuturage with the given email already exists", ENTITY_NAME, "emailexists");
        }

        // Get the current logged in user and assign it to Umuturage
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        User user = userRepository.findByLogin(username);
        umuturage.setUser(user);

        Umuturage result = umuturageService.save(umuturage);
        return ResponseEntity
            .created(new URI("/api/umuturages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /umuturages/:id} : Updates an existing umuturage.
     *
     * @param id the id of the umuturage to save.
     * @param umuturage the umuturage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated umuturage,
     * or with status {@code 400 (Bad Request)} if the umuturage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the umuturage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/umuturages/{id}")
    public ResponseEntity<Umuturage> updateUmuturage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Umuturage umuturage
    ) throws URISyntaxException {
        log.debug("REST request to update Umuturage : {}, {}", id, umuturage);
        if (umuturage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umuturage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!umuturageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Umuturage result = umuturageService.update(umuturage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, umuturage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /umuturages/:id} : Partial updates given fields of an existing umuturage, field will ignore if it is null
     *
     * @param id the id of the umuturage to save.
     * @param umuturage the umuturage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated umuturage,
     * or with status {@code 400 (Bad Request)} if the umuturage is not valid,
     * or with status {@code 404 (Not Found)} if the umuturage is not found,
     * or with status {@code 500 (Internal Server Error)} if the umuturage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/umuturages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Umuturage> partialUpdateUmuturage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Umuturage umuturage
    ) throws URISyntaxException {
        log.debug("REST request to partial update Umuturage partially : {}, {}", id, umuturage);
        if (umuturage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umuturage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!umuturageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Umuturage> result = umuturageService.partialUpdate(umuturage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, umuturage.getId().toString())
        );
    }

    /**
     * {@code GET  /umuturages} : get all the umuturages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of umuturages in body.
     */
    @GetMapping("/umuturages")
    public ResponseEntity<List<Umuturage>> getAllUmuturages(
        UmuturageCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Umuturages by criteria: {}", criteria);
        Page<Umuturage> page = umuturageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /umuturages/count} : count all the umuturages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/umuturages/count")
    public ResponseEntity<Long> countUmuturages(UmuturageCriteria criteria) {
        log.debug("REST request to count Umuturages by criteria: {}", criteria);
        return ResponseEntity.ok().body(umuturageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /umuturages/:id} : get the "id" umuturage.
     *
     * @param id the id of the umuturage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the umuturage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/umuturages/{id}")
    public ResponseEntity<Umuturage> getUmuturage(@PathVariable Long id) {
        log.debug("REST request to get Umuturage : {}", id);
        Optional<Umuturage> umuturage = umuturageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(umuturage);
    }

    /**
     * {@code DELETE  /umuturages/:id} : delete the "id" umuturage.
     *
     * @param id the id of the umuturage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/umuturages/{id}")
    public ResponseEntity<Void> deleteUmuturage(@PathVariable Long id) {
        log.debug("REST request to delete Umuturage : {}", id);
        umuturageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/umuturages/indentification/{identityId}")
    public Object getUmuturageByIdentityCard(@PathVariable("identityId") String identityCard) {
        Optional<Umuturage> umuturage = umuturageRepository.findByIndangamuntu(identityCard);
        if (umuturage.isPresent()) {
            throw new BadRequestAlertException(
                "Umuturage with the given indangamuntu already exists in our system.",
                ENTITY_NAME,
                "idexists"
            );
        }
        return umuturageService.getByIdentityCard(identityCard);
    }
}
