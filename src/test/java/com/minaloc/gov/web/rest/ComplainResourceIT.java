package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Category;
import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.domain.Organization;
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.domain.User;
import com.minaloc.gov.domain.enumeration.Priority;
import com.minaloc.gov.domain.enumeration.Status;
import com.minaloc.gov.repository.ComplainRepository;
import com.minaloc.gov.service.ComplainService;
import com.minaloc.gov.service.criteria.ComplainCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ComplainResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ComplainResourceIT {

    private static final String DEFAULT_IKIBAZO = "AAAAAAAAAA";
    private static final String UPDATED_IKIBAZO = "BBBBBBBBBB";

    private static final String DEFAULT_ICYAKOZWE = "AAAAAAAAAA";
    private static final String UPDATED_ICYAKOZWE = "BBBBBBBBBB";

    private static final String DEFAULT_ICYAKORWA = "AAAAAAAAAA";
    private static final String UPDATED_ICYAKORWA = "BBBBBBBBBB";

    private static final String DEFAULT_UMWANZURO = "AAAAAAAAAA";
    private static final String UPDATED_UMWANZURO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.ORIENTED;

    private static final Priority DEFAULT_PRIORITY = Priority.HIGH;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/complains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComplainRepository complainRepository;

    @Mock
    private ComplainRepository complainRepositoryMock;

    @Mock
    private ComplainService complainServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplainMockMvc;

    private Complain complain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complain createEntity(EntityManager em) {
        Complain complain = new Complain()
            .ikibazo(DEFAULT_IKIBAZO)
            .icyakozwe(DEFAULT_ICYAKOZWE)
            .icyakorwa(DEFAULT_ICYAKORWA)
            .umwanzuro(DEFAULT_UMWANZURO)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return complain;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Complain createUpdatedEntity(EntityManager em) {
        Complain complain = new Complain()
            .ikibazo(UPDATED_IKIBAZO)
            .icyakozwe(UPDATED_ICYAKOZWE)
            .icyakorwa(UPDATED_ICYAKORWA)
            .umwanzuro(UPDATED_UMWANZURO)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return complain;
    }

    @BeforeEach
    public void initTest() {
        complain = createEntity(em);
    }

    @Test
    @Transactional
    void createComplain() throws Exception {
        int databaseSizeBeforeCreate = complainRepository.findAll().size();
        // Create the Complain
        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isCreated());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeCreate + 1);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getIkibazo()).isEqualTo(DEFAULT_IKIBAZO);
        assertThat(testComplain.getIcyakozwe()).isEqualTo(DEFAULT_ICYAKOZWE);
        assertThat(testComplain.getIcyakorwa()).isEqualTo(DEFAULT_ICYAKORWA);
        assertThat(testComplain.getUmwanzuro()).isEqualTo(DEFAULT_UMWANZURO);
        assertThat(testComplain.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testComplain.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testComplain.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testComplain.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createComplainWithExistingId() throws Exception {
        // Create the Complain with an existing ID
        complain.setId(1L);

        int databaseSizeBeforeCreate = complainRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = complainRepository.findAll().size();
        // set the field null
        complain.setPriority(null);

        // Create the Complain, which fails.

        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = complainRepository.findAll().size();
        // set the field null
        complain.setCreatedAt(null);

        // Create the Complain, which fails.

        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = complainRepository.findAll().size();
        // set the field null
        complain.setUpdatedAt(null);

        // Create the Complain, which fails.

        restComplainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isBadRequest());

        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComplains() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complain.getId().intValue())))
            .andExpect(jsonPath("$.[*].ikibazo").value(hasItem(DEFAULT_IKIBAZO.toString())))
            .andExpect(jsonPath("$.[*].icyakozwe").value(hasItem(DEFAULT_ICYAKOZWE.toString())))
            .andExpect(jsonPath("$.[*].icyakorwa").value(hasItem(DEFAULT_ICYAKORWA.toString())))
            .andExpect(jsonPath("$.[*].umwanzuro").value(hasItem(DEFAULT_UMWANZURO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComplainsWithEagerRelationshipsIsEnabled() throws Exception {
        when(complainServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComplainMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(complainServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComplainsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(complainServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComplainMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(complainServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get the complain
        restComplainMockMvc
            .perform(get(ENTITY_API_URL_ID, complain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complain.getId().intValue()))
            .andExpect(jsonPath("$.ikibazo").value(DEFAULT_IKIBAZO.toString()))
            .andExpect(jsonPath("$.icyakozwe").value(DEFAULT_ICYAKOZWE.toString()))
            .andExpect(jsonPath("$.icyakorwa").value(DEFAULT_ICYAKORWA.toString()))
            .andExpect(jsonPath("$.umwanzuro").value(DEFAULT_UMWANZURO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getComplainsByIdFiltering() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        Long id = complain.getId();

        defaultComplainShouldBeFound("id.equals=" + id);
        defaultComplainShouldNotBeFound("id.notEquals=" + id);

        defaultComplainShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComplainShouldNotBeFound("id.greaterThan=" + id);

        defaultComplainShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComplainShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComplainsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where status equals to DEFAULT_STATUS
        defaultComplainShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the complainList where status equals to UPDATED_STATUS
        defaultComplainShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllComplainsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where status not equals to DEFAULT_STATUS
        defaultComplainShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the complainList where status not equals to UPDATED_STATUS
        defaultComplainShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllComplainsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultComplainShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the complainList where status equals to UPDATED_STATUS
        defaultComplainShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllComplainsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where status is not null
        defaultComplainShouldBeFound("status.specified=true");

        // Get all the complainList where status is null
        defaultComplainShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where priority equals to DEFAULT_PRIORITY
        defaultComplainShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the complainList where priority equals to UPDATED_PRIORITY
        defaultComplainShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllComplainsByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where priority not equals to DEFAULT_PRIORITY
        defaultComplainShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the complainList where priority not equals to UPDATED_PRIORITY
        defaultComplainShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllComplainsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultComplainShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the complainList where priority equals to UPDATED_PRIORITY
        defaultComplainShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllComplainsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where priority is not null
        defaultComplainShouldBeFound("priority.specified=true");

        // Get all the complainList where priority is null
        defaultComplainShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where createdAt equals to DEFAULT_CREATED_AT
        defaultComplainShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the complainList where createdAt equals to UPDATED_CREATED_AT
        defaultComplainShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllComplainsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where createdAt not equals to DEFAULT_CREATED_AT
        defaultComplainShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the complainList where createdAt not equals to UPDATED_CREATED_AT
        defaultComplainShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllComplainsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultComplainShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the complainList where createdAt equals to UPDATED_CREATED_AT
        defaultComplainShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllComplainsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where createdAt is not null
        defaultComplainShouldBeFound("createdAt.specified=true");

        // Get all the complainList where createdAt is null
        defaultComplainShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultComplainShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the complainList where updatedAt equals to UPDATED_UPDATED_AT
        defaultComplainShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllComplainsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultComplainShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the complainList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultComplainShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllComplainsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultComplainShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the complainList where updatedAt equals to UPDATED_UPDATED_AT
        defaultComplainShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllComplainsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        // Get all the complainList where updatedAt is not null
        defaultComplainShouldBeFound("updatedAt.specified=true");

        // Get all the complainList where updatedAt is null
        defaultComplainShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllComplainsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        complain.setCategory(category);
        complainRepository.saveAndFlush(complain);
        Long categoryId = category.getId();

        // Get all the complainList where category equals to categoryId
        defaultComplainShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the complainList where category equals to (categoryId + 1)
        defaultComplainShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllComplainsByUmuturageIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Umuturage umuturage;
        if (TestUtil.findAll(em, Umuturage.class).isEmpty()) {
            umuturage = UmuturageResourceIT.createEntity(em);
            em.persist(umuturage);
            em.flush();
        } else {
            umuturage = TestUtil.findAll(em, Umuturage.class).get(0);
        }
        em.persist(umuturage);
        em.flush();
        complain.setUmuturage(umuturage);
        complainRepository.saveAndFlush(complain);
        Long umuturageId = umuturage.getId();

        // Get all the complainList where umuturage equals to umuturageId
        defaultComplainShouldBeFound("umuturageId.equals=" + umuturageId);

        // Get all the complainList where umuturage equals to (umuturageId + 1)
        defaultComplainShouldNotBeFound("umuturageId.equals=" + (umuturageId + 1));
    }

    @Test
    @Transactional
    void getAllComplainsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        complain.setUser(user);
        complainRepository.saveAndFlush(complain);
        Long userId = user.getId();

        // Get all the complainList where user equals to userId
        defaultComplainShouldBeFound("userId.equals=" + userId);

        // Get all the complainList where user equals to (userId + 1)
        defaultComplainShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllComplainsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);
        Organization organization;
        if (TestUtil.findAll(em, Organization.class).isEmpty()) {
            organization = OrganizationResourceIT.createEntity(em);
            em.persist(organization);
            em.flush();
        } else {
            organization = TestUtil.findAll(em, Organization.class).get(0);
        }
        em.persist(organization);
        em.flush();
        complain.addOrganization(organization);
        complainRepository.saveAndFlush(complain);
        Long organizationId = organization.getId();

        // Get all the complainList where organization equals to organizationId
        defaultComplainShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the complainList where organization equals to (organizationId + 1)
        defaultComplainShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComplainShouldBeFound(String filter) throws Exception {
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complain.getId().intValue())))
            .andExpect(jsonPath("$.[*].ikibazo").value(hasItem(DEFAULT_IKIBAZO.toString())))
            .andExpect(jsonPath("$.[*].icyakozwe").value(hasItem(DEFAULT_ICYAKOZWE.toString())))
            .andExpect(jsonPath("$.[*].icyakorwa").value(hasItem(DEFAULT_ICYAKORWA.toString())))
            .andExpect(jsonPath("$.[*].umwanzuro").value(hasItem(DEFAULT_UMWANZURO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComplainShouldNotBeFound(String filter) throws Exception {
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComplainMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComplain() throws Exception {
        // Get the complain
        restComplainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain
        Complain updatedComplain = complainRepository.findById(complain.getId()).get();
        // Disconnect from session so that the updates on updatedComplain are not directly saved in db
        em.detach(updatedComplain);
        updatedComplain
            .ikibazo(UPDATED_IKIBAZO)
            .icyakozwe(UPDATED_ICYAKOZWE)
            .icyakorwa(UPDATED_ICYAKORWA)
            .umwanzuro(UPDATED_UMWANZURO)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restComplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComplain.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComplain))
            )
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getIkibazo()).isEqualTo(UPDATED_IKIBAZO);
        assertThat(testComplain.getIcyakozwe()).isEqualTo(UPDATED_ICYAKOZWE);
        assertThat(testComplain.getIcyakorwa()).isEqualTo(UPDATED_ICYAKORWA);
        assertThat(testComplain.getUmwanzuro()).isEqualTo(UPDATED_UMWANZURO);
        assertThat(testComplain.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testComplain.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testComplain.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testComplain.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complain.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComplainWithPatch() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain using partial update
        Complain partialUpdatedComplain = new Complain();
        partialUpdatedComplain.setId(complain.getId());

        partialUpdatedComplain
            .ikibazo(UPDATED_IKIBAZO)
            .icyakozwe(UPDATED_ICYAKOZWE)
            .icyakorwa(UPDATED_ICYAKORWA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplain))
            )
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getIkibazo()).isEqualTo(UPDATED_IKIBAZO);
        assertThat(testComplain.getIcyakozwe()).isEqualTo(UPDATED_ICYAKOZWE);
        assertThat(testComplain.getIcyakorwa()).isEqualTo(UPDATED_ICYAKORWA);
        assertThat(testComplain.getUmwanzuro()).isEqualTo(DEFAULT_UMWANZURO);
        assertThat(testComplain.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testComplain.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testComplain.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testComplain.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateComplainWithPatch() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeUpdate = complainRepository.findAll().size();

        // Update the complain using partial update
        Complain partialUpdatedComplain = new Complain();
        partialUpdatedComplain.setId(complain.getId());

        partialUpdatedComplain
            .ikibazo(UPDATED_IKIBAZO)
            .icyakozwe(UPDATED_ICYAKOZWE)
            .icyakorwa(UPDATED_ICYAKORWA)
            .umwanzuro(UPDATED_UMWANZURO)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplain))
            )
            .andExpect(status().isOk());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
        Complain testComplain = complainList.get(complainList.size() - 1);
        assertThat(testComplain.getIkibazo()).isEqualTo(UPDATED_IKIBAZO);
        assertThat(testComplain.getIcyakozwe()).isEqualTo(UPDATED_ICYAKOZWE);
        assertThat(testComplain.getIcyakorwa()).isEqualTo(UPDATED_ICYAKORWA);
        assertThat(testComplain.getUmwanzuro()).isEqualTo(UPDATED_UMWANZURO);
        assertThat(testComplain.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testComplain.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testComplain.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testComplain.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, complain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComplain() throws Exception {
        int databaseSizeBeforeUpdate = complainRepository.findAll().size();
        complain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplainMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(complain)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Complain in the database
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComplain() throws Exception {
        // Initialize the database
        complainRepository.saveAndFlush(complain);

        int databaseSizeBeforeDelete = complainRepository.findAll().size();

        // Delete the complain
        restComplainMockMvc
            .perform(delete(ENTITY_API_URL_ID, complain.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Complain> complainList = complainRepository.findAll();
        assertThat(complainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
