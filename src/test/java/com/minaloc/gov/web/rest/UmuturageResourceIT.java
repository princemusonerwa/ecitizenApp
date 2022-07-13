package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.domain.User;
import com.minaloc.gov.domain.Village;
import com.minaloc.gov.domain.enumeration.Gender;
import com.minaloc.gov.repository.UmuturageRepository;
import com.minaloc.gov.service.UmuturageService;
import com.minaloc.gov.service.criteria.UmuturageCriteria;
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

/**
 * Integration tests for the {@link UmuturageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UmuturageResourceIT {

    private static final String DEFAULT_INDANGAMUNTU = "AAAAAAAAAAAAAAAA";
    private static final String UPDATED_INDANGAMUNTU = "BBBBBBBBBBBBBBBB";

    private static final String DEFAULT_AMAZINA = "AAAAAAAAAA";
    private static final String UPDATED_AMAZINA = "BBBBBBBBBB";

    private static final Instant DEFAULT_DOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOB = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_UBUDEHE_CATEGORY = "A";
    private static final String UPDATED_UBUDEHE_CATEGORY = "B";

    private static final String DEFAULT_PHONE = "AAAAAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "]@|Jm.>Uax";
    private static final String UPDATED_EMAIL = "G@[8^mC.\\qzr8C";

    private static final String ENTITY_API_URL = "/api/umuturages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UmuturageRepository umuturageRepository;

    @Mock
    private UmuturageRepository umuturageRepositoryMock;

    @Mock
    private UmuturageService umuturageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUmuturageMockMvc;

    private Umuturage umuturage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Umuturage createEntity(EntityManager em) {
        Umuturage umuturage = new Umuturage()
            .indangamuntu(DEFAULT_INDANGAMUNTU)
            .amazina(DEFAULT_AMAZINA)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .ubudeheCategory(DEFAULT_UBUDEHE_CATEGORY)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return umuturage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Umuturage createUpdatedEntity(EntityManager em) {
        Umuturage umuturage = new Umuturage()
            .indangamuntu(UPDATED_INDANGAMUNTU)
            .amazina(UPDATED_AMAZINA)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .ubudeheCategory(UPDATED_UBUDEHE_CATEGORY)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        return umuturage;
    }

    @BeforeEach
    public void initTest() {
        umuturage = createEntity(em);
    }

    @Test
    @Transactional
    void createUmuturage() throws Exception {
        int databaseSizeBeforeCreate = umuturageRepository.findAll().size();
        // Create the Umuturage
        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isCreated());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeCreate + 1);
        Umuturage testUmuturage = umuturageList.get(umuturageList.size() - 1);
        assertThat(testUmuturage.getIndangamuntu()).isEqualTo(DEFAULT_INDANGAMUNTU);
        assertThat(testUmuturage.getAmazina()).isEqualTo(DEFAULT_AMAZINA);
        assertThat(testUmuturage.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testUmuturage.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUmuturage.getUbudeheCategory()).isEqualTo(DEFAULT_UBUDEHE_CATEGORY);
        assertThat(testUmuturage.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUmuturage.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createUmuturageWithExistingId() throws Exception {
        // Create the Umuturage with an existing ID
        umuturage.setId(1L);

        int databaseSizeBeforeCreate = umuturageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIndangamuntuIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuturageRepository.findAll().size();
        // set the field null
        umuturage.setIndangamuntu(null);

        // Create the Umuturage, which fails.

        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmazinaIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuturageRepository.findAll().size();
        // set the field null
        umuturage.setAmazina(null);

        // Create the Umuturage, which fails.

        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuturageRepository.findAll().size();
        // set the field null
        umuturage.setDob(null);

        // Create the Umuturage, which fails.

        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuturageRepository.findAll().size();
        // set the field null
        umuturage.setGender(null);

        // Create the Umuturage, which fails.

        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUbudeheCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuturageRepository.findAll().size();
        // set the field null
        umuturage.setUbudeheCategory(null);

        // Create the Umuturage, which fails.

        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuturageRepository.findAll().size();
        // set the field null
        umuturage.setEmail(null);

        // Create the Umuturage, which fails.

        restUmuturageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isBadRequest());

        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUmuturages() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList
        restUmuturageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(umuturage.getId().intValue())))
            .andExpect(jsonPath("$.[*].indangamuntu").value(hasItem(DEFAULT_INDANGAMUNTU)))
            .andExpect(jsonPath("$.[*].amazina").value(hasItem(DEFAULT_AMAZINA)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].ubudeheCategory").value(hasItem(DEFAULT_UBUDEHE_CATEGORY)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUmuturagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(umuturageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUmuturageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(umuturageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUmuturagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(umuturageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUmuturageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(umuturageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUmuturage() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get the umuturage
        restUmuturageMockMvc
            .perform(get(ENTITY_API_URL_ID, umuturage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(umuturage.getId().intValue()))
            .andExpect(jsonPath("$.indangamuntu").value(DEFAULT_INDANGAMUNTU))
            .andExpect(jsonPath("$.amazina").value(DEFAULT_AMAZINA))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.ubudeheCategory").value(DEFAULT_UBUDEHE_CATEGORY))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getUmuturagesByIdFiltering() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        Long id = umuturage.getId();

        defaultUmuturageShouldBeFound("id.equals=" + id);
        defaultUmuturageShouldNotBeFound("id.notEquals=" + id);

        defaultUmuturageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUmuturageShouldNotBeFound("id.greaterThan=" + id);

        defaultUmuturageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUmuturageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUmuturagesByIndangamuntuIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where indangamuntu equals to DEFAULT_INDANGAMUNTU
        defaultUmuturageShouldBeFound("indangamuntu.equals=" + DEFAULT_INDANGAMUNTU);

        // Get all the umuturageList where indangamuntu equals to UPDATED_INDANGAMUNTU
        defaultUmuturageShouldNotBeFound("indangamuntu.equals=" + UPDATED_INDANGAMUNTU);
    }

    @Test
    @Transactional
    void getAllUmuturagesByIndangamuntuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where indangamuntu not equals to DEFAULT_INDANGAMUNTU
        defaultUmuturageShouldNotBeFound("indangamuntu.notEquals=" + DEFAULT_INDANGAMUNTU);

        // Get all the umuturageList where indangamuntu not equals to UPDATED_INDANGAMUNTU
        defaultUmuturageShouldBeFound("indangamuntu.notEquals=" + UPDATED_INDANGAMUNTU);
    }

    @Test
    @Transactional
    void getAllUmuturagesByIndangamuntuIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where indangamuntu in DEFAULT_INDANGAMUNTU or UPDATED_INDANGAMUNTU
        defaultUmuturageShouldBeFound("indangamuntu.in=" + DEFAULT_INDANGAMUNTU + "," + UPDATED_INDANGAMUNTU);

        // Get all the umuturageList where indangamuntu equals to UPDATED_INDANGAMUNTU
        defaultUmuturageShouldNotBeFound("indangamuntu.in=" + UPDATED_INDANGAMUNTU);
    }

    @Test
    @Transactional
    void getAllUmuturagesByIndangamuntuIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where indangamuntu is not null
        defaultUmuturageShouldBeFound("indangamuntu.specified=true");

        // Get all the umuturageList where indangamuntu is null
        defaultUmuturageShouldNotBeFound("indangamuntu.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByIndangamuntuContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where indangamuntu contains DEFAULT_INDANGAMUNTU
        defaultUmuturageShouldBeFound("indangamuntu.contains=" + DEFAULT_INDANGAMUNTU);

        // Get all the umuturageList where indangamuntu contains UPDATED_INDANGAMUNTU
        defaultUmuturageShouldNotBeFound("indangamuntu.contains=" + UPDATED_INDANGAMUNTU);
    }

    @Test
    @Transactional
    void getAllUmuturagesByIndangamuntuNotContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where indangamuntu does not contain DEFAULT_INDANGAMUNTU
        defaultUmuturageShouldNotBeFound("indangamuntu.doesNotContain=" + DEFAULT_INDANGAMUNTU);

        // Get all the umuturageList where indangamuntu does not contain UPDATED_INDANGAMUNTU
        defaultUmuturageShouldBeFound("indangamuntu.doesNotContain=" + UPDATED_INDANGAMUNTU);
    }

    @Test
    @Transactional
    void getAllUmuturagesByAmazinaIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where amazina equals to DEFAULT_AMAZINA
        defaultUmuturageShouldBeFound("amazina.equals=" + DEFAULT_AMAZINA);

        // Get all the umuturageList where amazina equals to UPDATED_AMAZINA
        defaultUmuturageShouldNotBeFound("amazina.equals=" + UPDATED_AMAZINA);
    }

    @Test
    @Transactional
    void getAllUmuturagesByAmazinaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where amazina not equals to DEFAULT_AMAZINA
        defaultUmuturageShouldNotBeFound("amazina.notEquals=" + DEFAULT_AMAZINA);

        // Get all the umuturageList where amazina not equals to UPDATED_AMAZINA
        defaultUmuturageShouldBeFound("amazina.notEquals=" + UPDATED_AMAZINA);
    }

    @Test
    @Transactional
    void getAllUmuturagesByAmazinaIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where amazina in DEFAULT_AMAZINA or UPDATED_AMAZINA
        defaultUmuturageShouldBeFound("amazina.in=" + DEFAULT_AMAZINA + "," + UPDATED_AMAZINA);

        // Get all the umuturageList where amazina equals to UPDATED_AMAZINA
        defaultUmuturageShouldNotBeFound("amazina.in=" + UPDATED_AMAZINA);
    }

    @Test
    @Transactional
    void getAllUmuturagesByAmazinaIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where amazina is not null
        defaultUmuturageShouldBeFound("amazina.specified=true");

        // Get all the umuturageList where amazina is null
        defaultUmuturageShouldNotBeFound("amazina.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByAmazinaContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where amazina contains DEFAULT_AMAZINA
        defaultUmuturageShouldBeFound("amazina.contains=" + DEFAULT_AMAZINA);

        // Get all the umuturageList where amazina contains UPDATED_AMAZINA
        defaultUmuturageShouldNotBeFound("amazina.contains=" + UPDATED_AMAZINA);
    }

    @Test
    @Transactional
    void getAllUmuturagesByAmazinaNotContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where amazina does not contain DEFAULT_AMAZINA
        defaultUmuturageShouldNotBeFound("amazina.doesNotContain=" + DEFAULT_AMAZINA);

        // Get all the umuturageList where amazina does not contain UPDATED_AMAZINA
        defaultUmuturageShouldBeFound("amazina.doesNotContain=" + UPDATED_AMAZINA);
    }

    @Test
    @Transactional
    void getAllUmuturagesByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where dob equals to DEFAULT_DOB
        defaultUmuturageShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the umuturageList where dob equals to UPDATED_DOB
        defaultUmuturageShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllUmuturagesByDobIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where dob not equals to DEFAULT_DOB
        defaultUmuturageShouldNotBeFound("dob.notEquals=" + DEFAULT_DOB);

        // Get all the umuturageList where dob not equals to UPDATED_DOB
        defaultUmuturageShouldBeFound("dob.notEquals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllUmuturagesByDobIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultUmuturageShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the umuturageList where dob equals to UPDATED_DOB
        defaultUmuturageShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    void getAllUmuturagesByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where dob is not null
        defaultUmuturageShouldBeFound("dob.specified=true");

        // Get all the umuturageList where dob is null
        defaultUmuturageShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where gender equals to DEFAULT_GENDER
        defaultUmuturageShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the umuturageList where gender equals to UPDATED_GENDER
        defaultUmuturageShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllUmuturagesByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where gender not equals to DEFAULT_GENDER
        defaultUmuturageShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the umuturageList where gender not equals to UPDATED_GENDER
        defaultUmuturageShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllUmuturagesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultUmuturageShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the umuturageList where gender equals to UPDATED_GENDER
        defaultUmuturageShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllUmuturagesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where gender is not null
        defaultUmuturageShouldBeFound("gender.specified=true");

        // Get all the umuturageList where gender is null
        defaultUmuturageShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByUbudeheCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where ubudeheCategory equals to DEFAULT_UBUDEHE_CATEGORY
        defaultUmuturageShouldBeFound("ubudeheCategory.equals=" + DEFAULT_UBUDEHE_CATEGORY);

        // Get all the umuturageList where ubudeheCategory equals to UPDATED_UBUDEHE_CATEGORY
        defaultUmuturageShouldNotBeFound("ubudeheCategory.equals=" + UPDATED_UBUDEHE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllUmuturagesByUbudeheCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where ubudeheCategory not equals to DEFAULT_UBUDEHE_CATEGORY
        defaultUmuturageShouldNotBeFound("ubudeheCategory.notEquals=" + DEFAULT_UBUDEHE_CATEGORY);

        // Get all the umuturageList where ubudeheCategory not equals to UPDATED_UBUDEHE_CATEGORY
        defaultUmuturageShouldBeFound("ubudeheCategory.notEquals=" + UPDATED_UBUDEHE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllUmuturagesByUbudeheCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where ubudeheCategory in DEFAULT_UBUDEHE_CATEGORY or UPDATED_UBUDEHE_CATEGORY
        defaultUmuturageShouldBeFound("ubudeheCategory.in=" + DEFAULT_UBUDEHE_CATEGORY + "," + UPDATED_UBUDEHE_CATEGORY);

        // Get all the umuturageList where ubudeheCategory equals to UPDATED_UBUDEHE_CATEGORY
        defaultUmuturageShouldNotBeFound("ubudeheCategory.in=" + UPDATED_UBUDEHE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllUmuturagesByUbudeheCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where ubudeheCategory is not null
        defaultUmuturageShouldBeFound("ubudeheCategory.specified=true");

        // Get all the umuturageList where ubudeheCategory is null
        defaultUmuturageShouldNotBeFound("ubudeheCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByUbudeheCategoryContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where ubudeheCategory contains DEFAULT_UBUDEHE_CATEGORY
        defaultUmuturageShouldBeFound("ubudeheCategory.contains=" + DEFAULT_UBUDEHE_CATEGORY);

        // Get all the umuturageList where ubudeheCategory contains UPDATED_UBUDEHE_CATEGORY
        defaultUmuturageShouldNotBeFound("ubudeheCategory.contains=" + UPDATED_UBUDEHE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllUmuturagesByUbudeheCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where ubudeheCategory does not contain DEFAULT_UBUDEHE_CATEGORY
        defaultUmuturageShouldNotBeFound("ubudeheCategory.doesNotContain=" + DEFAULT_UBUDEHE_CATEGORY);

        // Get all the umuturageList where ubudeheCategory does not contain UPDATED_UBUDEHE_CATEGORY
        defaultUmuturageShouldBeFound("ubudeheCategory.doesNotContain=" + UPDATED_UBUDEHE_CATEGORY);
    }

    @Test
    @Transactional
    void getAllUmuturagesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where phone equals to DEFAULT_PHONE
        defaultUmuturageShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the umuturageList where phone equals to UPDATED_PHONE
        defaultUmuturageShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUmuturagesByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where phone not equals to DEFAULT_PHONE
        defaultUmuturageShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the umuturageList where phone not equals to UPDATED_PHONE
        defaultUmuturageShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUmuturagesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUmuturageShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the umuturageList where phone equals to UPDATED_PHONE
        defaultUmuturageShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUmuturagesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where phone is not null
        defaultUmuturageShouldBeFound("phone.specified=true");

        // Get all the umuturageList where phone is null
        defaultUmuturageShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where phone contains DEFAULT_PHONE
        defaultUmuturageShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the umuturageList where phone contains UPDATED_PHONE
        defaultUmuturageShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUmuturagesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where phone does not contain DEFAULT_PHONE
        defaultUmuturageShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the umuturageList where phone does not contain UPDATED_PHONE
        defaultUmuturageShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUmuturagesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where email equals to DEFAULT_EMAIL
        defaultUmuturageShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the umuturageList where email equals to UPDATED_EMAIL
        defaultUmuturageShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuturagesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where email not equals to DEFAULT_EMAIL
        defaultUmuturageShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the umuturageList where email not equals to UPDATED_EMAIL
        defaultUmuturageShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuturagesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUmuturageShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the umuturageList where email equals to UPDATED_EMAIL
        defaultUmuturageShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuturagesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where email is not null
        defaultUmuturageShouldBeFound("email.specified=true");

        // Get all the umuturageList where email is null
        defaultUmuturageShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuturagesByEmailContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where email contains DEFAULT_EMAIL
        defaultUmuturageShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the umuturageList where email contains UPDATED_EMAIL
        defaultUmuturageShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuturagesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        // Get all the umuturageList where email does not contain DEFAULT_EMAIL
        defaultUmuturageShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the umuturageList where email does not contain UPDATED_EMAIL
        defaultUmuturageShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuturagesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);
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
        umuturage.setUser(user);
        umuturageRepository.saveAndFlush(umuturage);
        Long userId = user.getId();

        // Get all the umuturageList where user equals to userId
        defaultUmuturageShouldBeFound("userId.equals=" + userId);

        // Get all the umuturageList where user equals to (userId + 1)
        defaultUmuturageShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUmuturagesByVillageIsEqualToSomething() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);
        Village village;
        if (TestUtil.findAll(em, Village.class).isEmpty()) {
            village = VillageResourceIT.createEntity(em);
            em.persist(village);
            em.flush();
        } else {
            village = TestUtil.findAll(em, Village.class).get(0);
        }
        em.persist(village);
        em.flush();
        umuturage.setVillage(village);
        umuturageRepository.saveAndFlush(umuturage);
        Long villageId = village.getId();

        // Get all the umuturageList where village equals to villageId
        defaultUmuturageShouldBeFound("villageId.equals=" + villageId);

        // Get all the umuturageList where village equals to (villageId + 1)
        defaultUmuturageShouldNotBeFound("villageId.equals=" + (villageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUmuturageShouldBeFound(String filter) throws Exception {
        restUmuturageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(umuturage.getId().intValue())))
            .andExpect(jsonPath("$.[*].indangamuntu").value(hasItem(DEFAULT_INDANGAMUNTU)))
            .andExpect(jsonPath("$.[*].amazina").value(hasItem(DEFAULT_AMAZINA)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].ubudeheCategory").value(hasItem(DEFAULT_UBUDEHE_CATEGORY)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restUmuturageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUmuturageShouldNotBeFound(String filter) throws Exception {
        restUmuturageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUmuturageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUmuturage() throws Exception {
        // Get the umuturage
        restUmuturageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUmuturage() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();

        // Update the umuturage
        Umuturage updatedUmuturage = umuturageRepository.findById(umuturage.getId()).get();
        // Disconnect from session so that the updates on updatedUmuturage are not directly saved in db
        em.detach(updatedUmuturage);
        updatedUmuturage
            .indangamuntu(UPDATED_INDANGAMUNTU)
            .amazina(UPDATED_AMAZINA)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .ubudeheCategory(UPDATED_UBUDEHE_CATEGORY)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restUmuturageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUmuturage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUmuturage))
            )
            .andExpect(status().isOk());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
        Umuturage testUmuturage = umuturageList.get(umuturageList.size() - 1);
        assertThat(testUmuturage.getIndangamuntu()).isEqualTo(UPDATED_INDANGAMUNTU);
        assertThat(testUmuturage.getAmazina()).isEqualTo(UPDATED_AMAZINA);
        assertThat(testUmuturage.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testUmuturage.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUmuturage.getUbudeheCategory()).isEqualTo(UPDATED_UBUDEHE_CATEGORY);
        assertThat(testUmuturage.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUmuturage.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingUmuturage() throws Exception {
        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();
        umuturage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUmuturageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, umuturage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(umuturage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUmuturage() throws Exception {
        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();
        umuturage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuturageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(umuturage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUmuturage() throws Exception {
        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();
        umuturage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuturageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuturage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUmuturageWithPatch() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();

        // Update the umuturage using partial update
        Umuturage partialUpdatedUmuturage = new Umuturage();
        partialUpdatedUmuturage.setId(umuturage.getId());

        partialUpdatedUmuturage
            .indangamuntu(UPDATED_INDANGAMUNTU)
            .amazina(UPDATED_AMAZINA)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restUmuturageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUmuturage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUmuturage))
            )
            .andExpect(status().isOk());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
        Umuturage testUmuturage = umuturageList.get(umuturageList.size() - 1);
        assertThat(testUmuturage.getIndangamuntu()).isEqualTo(UPDATED_INDANGAMUNTU);
        assertThat(testUmuturage.getAmazina()).isEqualTo(UPDATED_AMAZINA);
        assertThat(testUmuturage.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testUmuturage.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUmuturage.getUbudeheCategory()).isEqualTo(DEFAULT_UBUDEHE_CATEGORY);
        assertThat(testUmuturage.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUmuturage.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateUmuturageWithPatch() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();

        // Update the umuturage using partial update
        Umuturage partialUpdatedUmuturage = new Umuturage();
        partialUpdatedUmuturage.setId(umuturage.getId());

        partialUpdatedUmuturage
            .indangamuntu(UPDATED_INDANGAMUNTU)
            .amazina(UPDATED_AMAZINA)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .ubudeheCategory(UPDATED_UBUDEHE_CATEGORY)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restUmuturageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUmuturage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUmuturage))
            )
            .andExpect(status().isOk());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
        Umuturage testUmuturage = umuturageList.get(umuturageList.size() - 1);
        assertThat(testUmuturage.getIndangamuntu()).isEqualTo(UPDATED_INDANGAMUNTU);
        assertThat(testUmuturage.getAmazina()).isEqualTo(UPDATED_AMAZINA);
        assertThat(testUmuturage.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testUmuturage.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUmuturage.getUbudeheCategory()).isEqualTo(UPDATED_UBUDEHE_CATEGORY);
        assertThat(testUmuturage.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUmuturage.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingUmuturage() throws Exception {
        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();
        umuturage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUmuturageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, umuturage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(umuturage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUmuturage() throws Exception {
        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();
        umuturage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuturageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(umuturage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUmuturage() throws Exception {
        int databaseSizeBeforeUpdate = umuturageRepository.findAll().size();
        umuturage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuturageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(umuturage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Umuturage in the database
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUmuturage() throws Exception {
        // Initialize the database
        umuturageRepository.saveAndFlush(umuturage);

        int databaseSizeBeforeDelete = umuturageRepository.findAll().size();

        // Delete the umuturage
        restUmuturageMockMvc
            .perform(delete(ENTITY_API_URL_ID, umuturage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Umuturage> umuturageList = umuturageRepository.findAll();
        assertThat(umuturageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
