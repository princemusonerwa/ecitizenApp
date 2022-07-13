package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Umurimo;
import com.minaloc.gov.domain.Umuyobozi;
import com.minaloc.gov.repository.UmuyoboziRepository;
import com.minaloc.gov.service.UmuyoboziService;
import com.minaloc.gov.service.criteria.UmuyoboziCriteria;
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
 * Integration tests for the {@link UmuyoboziResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UmuyoboziResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_ONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_ONE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_TWO = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_TWO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "2\\D@Ho..!II";
    private static final String UPDATED_EMAIL = "[T@+?.C";

    private static final String ENTITY_API_URL = "/api/umuyobozis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UmuyoboziRepository umuyoboziRepository;

    @Mock
    private UmuyoboziRepository umuyoboziRepositoryMock;

    @Mock
    private UmuyoboziService umuyoboziServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUmuyoboziMockMvc;

    private Umuyobozi umuyobozi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Umuyobozi createEntity(EntityManager em) {
        Umuyobozi umuyobozi = new Umuyobozi()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneOne(DEFAULT_PHONE_ONE)
            .phoneTwo(DEFAULT_PHONE_TWO)
            .email(DEFAULT_EMAIL);
        return umuyobozi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Umuyobozi createUpdatedEntity(EntityManager em) {
        Umuyobozi umuyobozi = new Umuyobozi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneOne(UPDATED_PHONE_ONE)
            .phoneTwo(UPDATED_PHONE_TWO)
            .email(UPDATED_EMAIL);
        return umuyobozi;
    }

    @BeforeEach
    public void initTest() {
        umuyobozi = createEntity(em);
    }

    @Test
    @Transactional
    void createUmuyobozi() throws Exception {
        int databaseSizeBeforeCreate = umuyoboziRepository.findAll().size();
        // Create the Umuyobozi
        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isCreated());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeCreate + 1);
        Umuyobozi testUmuyobozi = umuyoboziList.get(umuyoboziList.size() - 1);
        assertThat(testUmuyobozi.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUmuyobozi.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUmuyobozi.getPhoneOne()).isEqualTo(DEFAULT_PHONE_ONE);
        assertThat(testUmuyobozi.getPhoneTwo()).isEqualTo(DEFAULT_PHONE_TWO);
        assertThat(testUmuyobozi.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createUmuyoboziWithExistingId() throws Exception {
        // Create the Umuyobozi with an existing ID
        umuyobozi.setId(1L);

        int databaseSizeBeforeCreate = umuyoboziRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isBadRequest());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuyoboziRepository.findAll().size();
        // set the field null
        umuyobozi.setFirstName(null);

        // Create the Umuyobozi, which fails.

        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isBadRequest());

        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuyoboziRepository.findAll().size();
        // set the field null
        umuyobozi.setLastName(null);

        // Create the Umuyobozi, which fails.

        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isBadRequest());

        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneOneIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuyoboziRepository.findAll().size();
        // set the field null
        umuyobozi.setPhoneOne(null);

        // Create the Umuyobozi, which fails.

        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isBadRequest());

        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneTwoIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuyoboziRepository.findAll().size();
        // set the field null
        umuyobozi.setPhoneTwo(null);

        // Create the Umuyobozi, which fails.

        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isBadRequest());

        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = umuyoboziRepository.findAll().size();
        // set the field null
        umuyobozi.setEmail(null);

        // Create the Umuyobozi, which fails.

        restUmuyoboziMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isBadRequest());

        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUmuyobozis() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList
        restUmuyoboziMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(umuyobozi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneOne").value(hasItem(DEFAULT_PHONE_ONE)))
            .andExpect(jsonPath("$.[*].phoneTwo").value(hasItem(DEFAULT_PHONE_TWO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUmuyobozisWithEagerRelationshipsIsEnabled() throws Exception {
        when(umuyoboziServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUmuyoboziMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(umuyoboziServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUmuyobozisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(umuyoboziServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUmuyoboziMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(umuyoboziServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUmuyobozi() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get the umuyobozi
        restUmuyoboziMockMvc
            .perform(get(ENTITY_API_URL_ID, umuyobozi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(umuyobozi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneOne").value(DEFAULT_PHONE_ONE))
            .andExpect(jsonPath("$.phoneTwo").value(DEFAULT_PHONE_TWO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getUmuyobozisByIdFiltering() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        Long id = umuyobozi.getId();

        defaultUmuyoboziShouldBeFound("id.equals=" + id);
        defaultUmuyoboziShouldNotBeFound("id.notEquals=" + id);

        defaultUmuyoboziShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUmuyoboziShouldNotBeFound("id.greaterThan=" + id);

        defaultUmuyoboziShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUmuyoboziShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where firstName equals to DEFAULT_FIRST_NAME
        defaultUmuyoboziShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the umuyoboziList where firstName equals to UPDATED_FIRST_NAME
        defaultUmuyoboziShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where firstName not equals to DEFAULT_FIRST_NAME
        defaultUmuyoboziShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the umuyoboziList where firstName not equals to UPDATED_FIRST_NAME
        defaultUmuyoboziShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUmuyoboziShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the umuyoboziList where firstName equals to UPDATED_FIRST_NAME
        defaultUmuyoboziShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where firstName is not null
        defaultUmuyoboziShouldBeFound("firstName.specified=true");

        // Get all the umuyoboziList where firstName is null
        defaultUmuyoboziShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuyobozisByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where firstName contains DEFAULT_FIRST_NAME
        defaultUmuyoboziShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the umuyoboziList where firstName contains UPDATED_FIRST_NAME
        defaultUmuyoboziShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUmuyoboziShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the umuyoboziList where firstName does not contain UPDATED_FIRST_NAME
        defaultUmuyoboziShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where lastName equals to DEFAULT_LAST_NAME
        defaultUmuyoboziShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the umuyoboziList where lastName equals to UPDATED_LAST_NAME
        defaultUmuyoboziShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where lastName not equals to DEFAULT_LAST_NAME
        defaultUmuyoboziShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the umuyoboziList where lastName not equals to UPDATED_LAST_NAME
        defaultUmuyoboziShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUmuyoboziShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the umuyoboziList where lastName equals to UPDATED_LAST_NAME
        defaultUmuyoboziShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where lastName is not null
        defaultUmuyoboziShouldBeFound("lastName.specified=true");

        // Get all the umuyoboziList where lastName is null
        defaultUmuyoboziShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuyobozisByLastNameContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where lastName contains DEFAULT_LAST_NAME
        defaultUmuyoboziShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the umuyoboziList where lastName contains UPDATED_LAST_NAME
        defaultUmuyoboziShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where lastName does not contain DEFAULT_LAST_NAME
        defaultUmuyoboziShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the umuyoboziList where lastName does not contain UPDATED_LAST_NAME
        defaultUmuyoboziShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneOneIsEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneOne equals to DEFAULT_PHONE_ONE
        defaultUmuyoboziShouldBeFound("phoneOne.equals=" + DEFAULT_PHONE_ONE);

        // Get all the umuyoboziList where phoneOne equals to UPDATED_PHONE_ONE
        defaultUmuyoboziShouldNotBeFound("phoneOne.equals=" + UPDATED_PHONE_ONE);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneOneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneOne not equals to DEFAULT_PHONE_ONE
        defaultUmuyoboziShouldNotBeFound("phoneOne.notEquals=" + DEFAULT_PHONE_ONE);

        // Get all the umuyoboziList where phoneOne not equals to UPDATED_PHONE_ONE
        defaultUmuyoboziShouldBeFound("phoneOne.notEquals=" + UPDATED_PHONE_ONE);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneOneIsInShouldWork() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneOne in DEFAULT_PHONE_ONE or UPDATED_PHONE_ONE
        defaultUmuyoboziShouldBeFound("phoneOne.in=" + DEFAULT_PHONE_ONE + "," + UPDATED_PHONE_ONE);

        // Get all the umuyoboziList where phoneOne equals to UPDATED_PHONE_ONE
        defaultUmuyoboziShouldNotBeFound("phoneOne.in=" + UPDATED_PHONE_ONE);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneOneIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneOne is not null
        defaultUmuyoboziShouldBeFound("phoneOne.specified=true");

        // Get all the umuyoboziList where phoneOne is null
        defaultUmuyoboziShouldNotBeFound("phoneOne.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneOneContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneOne contains DEFAULT_PHONE_ONE
        defaultUmuyoboziShouldBeFound("phoneOne.contains=" + DEFAULT_PHONE_ONE);

        // Get all the umuyoboziList where phoneOne contains UPDATED_PHONE_ONE
        defaultUmuyoboziShouldNotBeFound("phoneOne.contains=" + UPDATED_PHONE_ONE);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneOneNotContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneOne does not contain DEFAULT_PHONE_ONE
        defaultUmuyoboziShouldNotBeFound("phoneOne.doesNotContain=" + DEFAULT_PHONE_ONE);

        // Get all the umuyoboziList where phoneOne does not contain UPDATED_PHONE_ONE
        defaultUmuyoboziShouldBeFound("phoneOne.doesNotContain=" + UPDATED_PHONE_ONE);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneTwoIsEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneTwo equals to DEFAULT_PHONE_TWO
        defaultUmuyoboziShouldBeFound("phoneTwo.equals=" + DEFAULT_PHONE_TWO);

        // Get all the umuyoboziList where phoneTwo equals to UPDATED_PHONE_TWO
        defaultUmuyoboziShouldNotBeFound("phoneTwo.equals=" + UPDATED_PHONE_TWO);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneTwoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneTwo not equals to DEFAULT_PHONE_TWO
        defaultUmuyoboziShouldNotBeFound("phoneTwo.notEquals=" + DEFAULT_PHONE_TWO);

        // Get all the umuyoboziList where phoneTwo not equals to UPDATED_PHONE_TWO
        defaultUmuyoboziShouldBeFound("phoneTwo.notEquals=" + UPDATED_PHONE_TWO);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneTwoIsInShouldWork() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneTwo in DEFAULT_PHONE_TWO or UPDATED_PHONE_TWO
        defaultUmuyoboziShouldBeFound("phoneTwo.in=" + DEFAULT_PHONE_TWO + "," + UPDATED_PHONE_TWO);

        // Get all the umuyoboziList where phoneTwo equals to UPDATED_PHONE_TWO
        defaultUmuyoboziShouldNotBeFound("phoneTwo.in=" + UPDATED_PHONE_TWO);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneTwoIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneTwo is not null
        defaultUmuyoboziShouldBeFound("phoneTwo.specified=true");

        // Get all the umuyoboziList where phoneTwo is null
        defaultUmuyoboziShouldNotBeFound("phoneTwo.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneTwoContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneTwo contains DEFAULT_PHONE_TWO
        defaultUmuyoboziShouldBeFound("phoneTwo.contains=" + DEFAULT_PHONE_TWO);

        // Get all the umuyoboziList where phoneTwo contains UPDATED_PHONE_TWO
        defaultUmuyoboziShouldNotBeFound("phoneTwo.contains=" + UPDATED_PHONE_TWO);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByPhoneTwoNotContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where phoneTwo does not contain DEFAULT_PHONE_TWO
        defaultUmuyoboziShouldNotBeFound("phoneTwo.doesNotContain=" + DEFAULT_PHONE_TWO);

        // Get all the umuyoboziList where phoneTwo does not contain UPDATED_PHONE_TWO
        defaultUmuyoboziShouldBeFound("phoneTwo.doesNotContain=" + UPDATED_PHONE_TWO);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where email equals to DEFAULT_EMAIL
        defaultUmuyoboziShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the umuyoboziList where email equals to UPDATED_EMAIL
        defaultUmuyoboziShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where email not equals to DEFAULT_EMAIL
        defaultUmuyoboziShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the umuyoboziList where email not equals to UPDATED_EMAIL
        defaultUmuyoboziShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUmuyoboziShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the umuyoboziList where email equals to UPDATED_EMAIL
        defaultUmuyoboziShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where email is not null
        defaultUmuyoboziShouldBeFound("email.specified=true");

        // Get all the umuyoboziList where email is null
        defaultUmuyoboziShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUmuyobozisByEmailContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where email contains DEFAULT_EMAIL
        defaultUmuyoboziShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the umuyoboziList where email contains UPDATED_EMAIL
        defaultUmuyoboziShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        // Get all the umuyoboziList where email does not contain DEFAULT_EMAIL
        defaultUmuyoboziShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the umuyoboziList where email does not contain UPDATED_EMAIL
        defaultUmuyoboziShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUmuyobozisByUmurimoIsEqualToSomething() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);
        Umurimo umurimo;
        if (TestUtil.findAll(em, Umurimo.class).isEmpty()) {
            umurimo = UmurimoResourceIT.createEntity(em);
            em.persist(umurimo);
            em.flush();
        } else {
            umurimo = TestUtil.findAll(em, Umurimo.class).get(0);
        }
        em.persist(umurimo);
        em.flush();
        umuyobozi.setUmurimo(umurimo);
        umuyoboziRepository.saveAndFlush(umuyobozi);
        Long umurimoId = umurimo.getId();

        // Get all the umuyoboziList where umurimo equals to umurimoId
        defaultUmuyoboziShouldBeFound("umurimoId.equals=" + umurimoId);

        // Get all the umuyoboziList where umurimo equals to (umurimoId + 1)
        defaultUmuyoboziShouldNotBeFound("umurimoId.equals=" + (umurimoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUmuyoboziShouldBeFound(String filter) throws Exception {
        restUmuyoboziMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(umuyobozi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneOne").value(hasItem(DEFAULT_PHONE_ONE)))
            .andExpect(jsonPath("$.[*].phoneTwo").value(hasItem(DEFAULT_PHONE_TWO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restUmuyoboziMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUmuyoboziShouldNotBeFound(String filter) throws Exception {
        restUmuyoboziMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUmuyoboziMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUmuyobozi() throws Exception {
        // Get the umuyobozi
        restUmuyoboziMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUmuyobozi() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();

        // Update the umuyobozi
        Umuyobozi updatedUmuyobozi = umuyoboziRepository.findById(umuyobozi.getId()).get();
        // Disconnect from session so that the updates on updatedUmuyobozi are not directly saved in db
        em.detach(updatedUmuyobozi);
        updatedUmuyobozi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneOne(UPDATED_PHONE_ONE)
            .phoneTwo(UPDATED_PHONE_TWO)
            .email(UPDATED_EMAIL);

        restUmuyoboziMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUmuyobozi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUmuyobozi))
            )
            .andExpect(status().isOk());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
        Umuyobozi testUmuyobozi = umuyoboziList.get(umuyoboziList.size() - 1);
        assertThat(testUmuyobozi.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUmuyobozi.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUmuyobozi.getPhoneOne()).isEqualTo(UPDATED_PHONE_ONE);
        assertThat(testUmuyobozi.getPhoneTwo()).isEqualTo(UPDATED_PHONE_TWO);
        assertThat(testUmuyobozi.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingUmuyobozi() throws Exception {
        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();
        umuyobozi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUmuyoboziMockMvc
            .perform(
                put(ENTITY_API_URL_ID, umuyobozi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(umuyobozi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUmuyobozi() throws Exception {
        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();
        umuyobozi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuyoboziMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(umuyobozi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUmuyobozi() throws Exception {
        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();
        umuyobozi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuyoboziMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umuyobozi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUmuyoboziWithPatch() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();

        // Update the umuyobozi using partial update
        Umuyobozi partialUpdatedUmuyobozi = new Umuyobozi();
        partialUpdatedUmuyobozi.setId(umuyobozi.getId());

        partialUpdatedUmuyobozi.lastName(UPDATED_LAST_NAME).phoneTwo(UPDATED_PHONE_TWO).email(UPDATED_EMAIL);

        restUmuyoboziMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUmuyobozi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUmuyobozi))
            )
            .andExpect(status().isOk());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
        Umuyobozi testUmuyobozi = umuyoboziList.get(umuyoboziList.size() - 1);
        assertThat(testUmuyobozi.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUmuyobozi.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUmuyobozi.getPhoneOne()).isEqualTo(DEFAULT_PHONE_ONE);
        assertThat(testUmuyobozi.getPhoneTwo()).isEqualTo(UPDATED_PHONE_TWO);
        assertThat(testUmuyobozi.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateUmuyoboziWithPatch() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();

        // Update the umuyobozi using partial update
        Umuyobozi partialUpdatedUmuyobozi = new Umuyobozi();
        partialUpdatedUmuyobozi.setId(umuyobozi.getId());

        partialUpdatedUmuyobozi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneOne(UPDATED_PHONE_ONE)
            .phoneTwo(UPDATED_PHONE_TWO)
            .email(UPDATED_EMAIL);

        restUmuyoboziMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUmuyobozi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUmuyobozi))
            )
            .andExpect(status().isOk());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
        Umuyobozi testUmuyobozi = umuyoboziList.get(umuyoboziList.size() - 1);
        assertThat(testUmuyobozi.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUmuyobozi.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUmuyobozi.getPhoneOne()).isEqualTo(UPDATED_PHONE_ONE);
        assertThat(testUmuyobozi.getPhoneTwo()).isEqualTo(UPDATED_PHONE_TWO);
        assertThat(testUmuyobozi.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingUmuyobozi() throws Exception {
        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();
        umuyobozi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUmuyoboziMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, umuyobozi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(umuyobozi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUmuyobozi() throws Exception {
        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();
        umuyobozi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuyoboziMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(umuyobozi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUmuyobozi() throws Exception {
        int databaseSizeBeforeUpdate = umuyoboziRepository.findAll().size();
        umuyobozi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmuyoboziMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(umuyobozi))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Umuyobozi in the database
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUmuyobozi() throws Exception {
        // Initialize the database
        umuyoboziRepository.saveAndFlush(umuyobozi);

        int databaseSizeBeforeDelete = umuyoboziRepository.findAll().size();

        // Delete the umuyobozi
        restUmuyoboziMockMvc
            .perform(delete(ENTITY_API_URL_ID, umuyobozi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Umuyobozi> umuyoboziList = umuyoboziRepository.findAll();
        assertThat(umuyoboziList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
