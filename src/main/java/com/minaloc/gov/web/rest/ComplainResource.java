package com.minaloc.gov.web.rest;

import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.domain.User;
import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.repository.UmuturageRepository;
import com.minaloc.gov.repository.UserRepository;
import com.minaloc.gov.service.ComplainQueryService;
import com.minaloc.gov.service.ComplainService;
import com.minaloc.gov.service.EmailAlreadyUsedException;
import com.minaloc.gov.service.UmuturageService;
import com.minaloc.gov.service.criteria.ComplainCriteria;
import com.minaloc.gov.service.dto.UmuturageComplainDTO;
import com.minaloc.gov.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final UmuturageRepository umuturageRepository;

    private final UmuturageService umuturageService;

    public ComplainResource(
        ComplainService complainService,
        ComplainRepository complainRepository,
        ComplainQueryService complainQueryService,
        UserRepository userRepository,
        UmuturageRepository umuturageRepository,
        UmuturageService umuturageService
    ) {
        this.complainService = complainService;
        this.complainRepository = complainRepository;
        this.complainQueryService = complainQueryService;
        this.userRepository = userRepository;
        this.umuturageRepository = umuturageRepository;
        this.umuturageService = umuturageService;
    }

    /**
     * {@code POST  /complains} : Create a new complain.
     *
     * @param complain the complain to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complain, or with status {@code 400 (Bad Request)} if the complain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/complains")
    public ResponseEntity<Complain> createComplain(@Valid @RequestBody UmuturageComplainDTO umuturageComplainDTO)
        throws URISyntaxException {
        log.debug("REST request to save Complain : {}", umuturageComplainDTO);
        if (umuturageComplainDTO.getId() != null) {
            throw new BadRequestAlertException("A new complain cannot already have an ID", ENTITY_NAME, "id exists");
        }

        Optional<Umuturage> existingUmuturage = umuturageRepository.findByIndangamuntu(umuturageComplainDTO.getIndangamuntu());

        Optional<Umuturage> existingEmail = umuturageRepository.findOneByEmailIgnoreCase(umuturageComplainDTO.getEmail());

        Optional<Umuturage> existingPhone = umuturageRepository.findOneByEmailIgnoreCase(umuturageComplainDTO.getPhone());

        if (
            existingUmuturage.isPresent() &&
            (existingUmuturage.get().getIndangamuntu().equalsIgnoreCase(umuturageComplainDTO.getIndangamuntu()))
        ) {
            throw new BadRequestAlertException("Umuturage with the given indangamuntu already exists", ENTITY_NAME, "indangamuntuexists");
        }

        if (existingEmail.isPresent() && (existingEmail.get().getEmail().equalsIgnoreCase(umuturageComplainDTO.getEmail()))) {
            throw new BadRequestAlertException("Umuturage with the given email already exists", ENTITY_NAME, "emailexists");
        }

        if (existingPhone.isPresent() && (existingPhone.get().getEmail().equalsIgnoreCase(umuturageComplainDTO.getPhone()))) {
            throw new BadRequestAlertException("Umuturage with the given phone already exists", ENTITY_NAME, "phoneexists");
        }

        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        User user = userRepository.findByLogin(username);

        Umuturage umuturage = new Umuturage(
            umuturageComplainDTO.getIndangamuntu(),
            umuturageComplainDTO.getAmazina(),
            umuturageComplainDTO.getDob(),
            umuturageComplainDTO.getGender(),
            umuturageComplainDTO.getUbudeheCategory(),
            umuturageComplainDTO.getPhone(),
            umuturageComplainDTO.getEmail(),
            user,
            umuturageComplainDTO.getVillage()
        );
        umuturageRepository.save(umuturage);
        Complain complain = new Complain(
            umuturageComplainDTO.getIkibazo(),
            umuturageComplainDTO.getIcyakozwe(),
            umuturageComplainDTO.getIcyakorwa(),
            umuturageComplainDTO.getUmwanzuro(),
            umuturageComplainDTO.getStatus(),
            umuturageComplainDTO.getPriority(),
            umuturageComplainDTO.getCreatedAt(),
            umuturageComplainDTO.getUpdatedAt(),
            umuturageComplainDTO.getCategory(),
            umuturage,
            user,
            umuturageComplainDTO.getOrganizations()
        );
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
        @Valid @RequestBody UmuturageComplainDTO umuturageComplainDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Complain : {}, {}", id, umuturageComplainDTO);
        if (umuturageComplainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, umuturageComplainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Complain complain = complainRepository.getById(id);

        complain.setIkibazo(umuturageComplainDTO.getIkibazo());
        complain.setIcyakozwe(umuturageComplainDTO.getIcyakozwe());
        complain.setIcyakorwa(umuturageComplainDTO.getIcyakorwa());
        complain.setUmwanzuro(umuturageComplainDTO.getUmwanzuro());
        complain.setStatus(umuturageComplainDTO.getStatus());
        complain.setPriority(umuturageComplainDTO.getPriority());
        complain.setUpdatedAt(umuturageComplainDTO.getUpdatedAt());
        complain.setCategory(umuturageComplainDTO.getCategory());
        complain.setOrganizations(umuturageComplainDTO.getOrganizations());

        Umuturage umuturage = complain.getUmuturage();

        umuturage.setIndangamuntu(umuturageComplainDTO.getIndangamuntu());
        umuturage.setAmazina(umuturageComplainDTO.getAmazina());
        umuturage.setDob(umuturageComplainDTO.getDob());
        umuturage.setUbudeheCategory(umuturageComplainDTO.getUbudeheCategory());
        umuturage.setPhone(umuturageComplainDTO.getPhone());
        umuturage.setEmail(umuturageComplainDTO.getEmail());
        umuturage.setVillage(umuturageComplainDTO.getVillage());
        umuturageService.update(umuturage);

        complain.setUmuturage(umuturage);

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
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant startDate,
        @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant endDate
    ) {
        log.debug("REST request to get Complains by criteria: {}", criteria);
        Page<Complain> page;
        if (keyword != null) {
            page = complainRepository.findAll(pageable, keyword);
        } else if (startDate != null && endDate != null) {
            page = complainRepository.findAll(pageable, startDate, endDate);
        } else {
            page = complainQueryService.findByCriteria(criteria, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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

    @GetMapping("/complains/createdat")
    public ResponseEntity<Page<Complain>> findByCreatedAtBetween(
        Pageable pageable,
        @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant startDate,
        @RequestParam(value = "to") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant endDate
    ) {
        return ResponseEntity.ok().body(complainRepository.findAll(pageable, startDate, endDate));
    }
}
