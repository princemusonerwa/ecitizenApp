package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Umurimo;
import com.minaloc.gov.repository.UmurimoRepository;
import com.minaloc.gov.service.criteria.UmurimoCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UmurimoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UmurimoResourceIT {

    private static final String DEFAULT_UMURIMO = "AAAAAAAAAA";
    private static final String UPDATED_UMURIMO = "BBBBBBBBBB";

    private static final String DEFAULT_URWEGO = "AAAAAAAAAA";
    private static final String UPDATED_URWEGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/umurimos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UmurimoRepository umurimoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUmurimoMockMvc;

    private Umurimo umurimo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Umurimo createEntity(EntityManager em) {
        Umurimo umurimo = new Umurimo().umurimo(DEFAULT_UMURIMO).urwego(DEFAULT_URWEGO);
        return umurimo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Umurimo createUpdatedEntity(EntityManager em) {
        Umurimo umurimo = new Umurimo().umurimo(UPDATED_UMURIMO).urwego(UPDATED_URWEGO);
        return umurimo;
    }

    @BeforeEach
    public void initTest() {
        umurimo = createEntity(em);
    }

    @Test
    @Transactional
    void createUmurimo() throws Exception {
        int databaseSizeBeforeCreate = umurimoRepository.findAll().size();
        // Create the Umurimo
        restUmurimoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umurimo)))
            .andExpect(status().isCreated());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeCreate + 1);
        Umurimo testUmurimo = umurimoList.get(umurimoList.size() - 1);
        assertThat(testUmurimo.getUmurimo()).isEqualTo(DEFAULT_UMURIMO);
        assertThat(testUmurimo.getUrwego()).isEqualTo(DEFAULT_URWEGO);
    }

    @Test
    @Transactional
    void createUmurimoWithExistingId() throws Exception {
        // Create the Umurimo with an existing ID
        umurimo.setId(1L);

        int databaseSizeBeforeCreate = umurimoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUmurimoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umurimo)))
            .andExpect(status().isBadRequest());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUmurimoIsRequired() throws Exception {
        int databaseSizeBeforeTest = umurimoRepository.findAll().size();
        // set the field null
        umurimo.setUmurimo(null);

        // Create the Umurimo, which fails.

        restUmurimoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umurimo)))
            .andExpect(status().isBadRequest());

        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrwegoIsRequired() throws Exception {
        int databaseSizeBeforeTest = umurimoRepository.findAll().size();
        // set the field null
        umurimo.setUrwego(null);

        // Create the Umurimo, which fails.

        restUmurimoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umurimo)))
            .andExpect(status().isBadRequest());

        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUmurimos() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList
        restUmurimoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(umurimo.getId().intValue())))
            .andExpect(jsonPath("$.[*].umurimo").value(hasItem(DEFAULT_UMURIMO)))
            .andExpect(jsonPath("$.[*].urwego").value(hasItem(DEFAULT_URWEGO)));
    }

    @Test
    @Transactional
    void getUmurimo() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get the umurimo
        restUmurimoMockMvc
            .perform(get(ENTITY_API_URL_ID, umurimo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(umurimo.getId().intValue()))
            .andExpect(jsonPath("$.umurimo").value(DEFAULT_UMURIMO))
            .andExpect(jsonPath("$.urwego").value(DEFAULT_URWEGO));
    }

    @Test
    @Transactional
    void getUmurimosByIdFiltering() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        Long id = umurimo.getId();

        defaultUmurimoShouldBeFound("id.equals=" + id);
        defaultUmurimoShouldNotBeFound("id.notEquals=" + id);

        defaultUmurimoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUmurimoShouldNotBeFound("id.greaterThan=" + id);

        defaultUmurimoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUmurimoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUmurimosByUmurimoIsEqualToSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where umurimo equals to DEFAULT_UMURIMO
        defaultUmurimoShouldBeFound("umurimo.equals=" + DEFAULT_UMURIMO);

        // Get all the umurimoList where umurimo equals to UPDATED_UMURIMO
        defaultUmurimoShouldNotBeFound("umurimo.equals=" + UPDATED_UMURIMO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUmurimoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where umurimo not equals to DEFAULT_UMURIMO
        defaultUmurimoShouldNotBeFound("umurimo.notEquals=" + DEFAULT_UMURIMO);

        // Get all the umurimoList where umurimo not equals to UPDATED_UMURIMO
        defaultUmurimoShouldBeFound("umurimo.notEquals=" + UPDATED_UMURIMO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUmurimoIsInShouldWork() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where umurimo in DEFAULT_UMURIMO or UPDATED_UMURIMO
        defaultUmurimoShouldBeFound("umurimo.in=" + DEFAULT_UMURIMO + "," + UPDATED_UMURIMO);

        // Get all the umurimoList where umurimo equals to UPDATED_UMURIMO
        defaultUmurimoShouldNotBeFound("umurimo.in=" + UPDATED_UMURIMO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUmurimoIsNullOrNotNull() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where umurimo is not null
        defaultUmurimoShouldBeFound("umurimo.specified=true");

        // Get all the umurimoList where umurimo is null
        defaultUmurimoShouldNotBeFound("umurimo.specified=false");
    }

    @Test
    @Transactional
    void getAllUmurimosByUmurimoContainsSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where umurimo contains DEFAULT_UMURIMO
        defaultUmurimoShouldBeFound("umurimo.contains=" + DEFAULT_UMURIMO);

        // Get all the umurimoList where umurimo contains UPDATED_UMURIMO
        defaultUmurimoShouldNotBeFound("umurimo.contains=" + UPDATED_UMURIMO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUmurimoNotContainsSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where umurimo does not contain DEFAULT_UMURIMO
        defaultUmurimoShouldNotBeFound("umurimo.doesNotContain=" + DEFAULT_UMURIMO);

        // Get all the umurimoList where umurimo does not contain UPDATED_UMURIMO
        defaultUmurimoShouldBeFound("umurimo.doesNotContain=" + UPDATED_UMURIMO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUrwegoIsEqualToSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where urwego equals to DEFAULT_URWEGO
        defaultUmurimoShouldBeFound("urwego.equals=" + DEFAULT_URWEGO);

        // Get all the umurimoList where urwego equals to UPDATED_URWEGO
        defaultUmurimoShouldNotBeFound("urwego.equals=" + UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUrwegoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where urwego not equals to DEFAULT_URWEGO
        defaultUmurimoShouldNotBeFound("urwego.notEquals=" + DEFAULT_URWEGO);

        // Get all the umurimoList where urwego not equals to UPDATED_URWEGO
        defaultUmurimoShouldBeFound("urwego.notEquals=" + UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUrwegoIsInShouldWork() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where urwego in DEFAULT_URWEGO or UPDATED_URWEGO
        defaultUmurimoShouldBeFound("urwego.in=" + DEFAULT_URWEGO + "," + UPDATED_URWEGO);

        // Get all the umurimoList where urwego equals to UPDATED_URWEGO
        defaultUmurimoShouldNotBeFound("urwego.in=" + UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUrwegoIsNullOrNotNull() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where urwego is not null
        defaultUmurimoShouldBeFound("urwego.specified=true");

        // Get all the umurimoList where urwego is null
        defaultUmurimoShouldNotBeFound("urwego.specified=false");
    }

    @Test
    @Transactional
    void getAllUmurimosByUrwegoContainsSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where urwego contains DEFAULT_URWEGO
        defaultUmurimoShouldBeFound("urwego.contains=" + DEFAULT_URWEGO);

        // Get all the umurimoList where urwego contains UPDATED_URWEGO
        defaultUmurimoShouldNotBeFound("urwego.contains=" + UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void getAllUmurimosByUrwegoNotContainsSomething() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        // Get all the umurimoList where urwego does not contain DEFAULT_URWEGO
        defaultUmurimoShouldNotBeFound("urwego.doesNotContain=" + DEFAULT_URWEGO);

        // Get all the umurimoList where urwego does not contain UPDATED_URWEGO
        defaultUmurimoShouldBeFound("urwego.doesNotContain=" + UPDATED_URWEGO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUmurimoShouldBeFound(String filter) throws Exception {
        restUmurimoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(umurimo.getId().intValue())))
            .andExpect(jsonPath("$.[*].umurimo").value(hasItem(DEFAULT_UMURIMO)))
            .andExpect(jsonPath("$.[*].urwego").value(hasItem(DEFAULT_URWEGO)));

        // Check, that the count call also returns 1
        restUmurimoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUmurimoShouldNotBeFound(String filter) throws Exception {
        restUmurimoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUmurimoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUmurimo() throws Exception {
        // Get the umurimo
        restUmurimoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUmurimo() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();

        // Update the umurimo
        Umurimo updatedUmurimo = umurimoRepository.findById(umurimo.getId()).get();
        // Disconnect from session so that the updates on updatedUmurimo are not directly saved in db
        em.detach(updatedUmurimo);
        updatedUmurimo.umurimo(UPDATED_UMURIMO).urwego(UPDATED_URWEGO);

        restUmurimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUmurimo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUmurimo))
            )
            .andExpect(status().isOk());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
        Umurimo testUmurimo = umurimoList.get(umurimoList.size() - 1);
        assertThat(testUmurimo.getUmurimo()).isEqualTo(UPDATED_UMURIMO);
        assertThat(testUmurimo.getUrwego()).isEqualTo(UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void putNonExistingUmurimo() throws Exception {
        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();
        umurimo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUmurimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, umurimo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(umurimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUmurimo() throws Exception {
        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();
        umurimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmurimoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(umurimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUmurimo() throws Exception {
        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();
        umurimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmurimoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(umurimo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUmurimoWithPatch() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();

        // Update the umurimo using partial update
        Umurimo partialUpdatedUmurimo = new Umurimo();
        partialUpdatedUmurimo.setId(umurimo.getId());

        partialUpdatedUmurimo.umurimo(UPDATED_UMURIMO).urwego(UPDATED_URWEGO);

        restUmurimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUmurimo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUmurimo))
            )
            .andExpect(status().isOk());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
        Umurimo testUmurimo = umurimoList.get(umurimoList.size() - 1);
        assertThat(testUmurimo.getUmurimo()).isEqualTo(UPDATED_UMURIMO);
        assertThat(testUmurimo.getUrwego()).isEqualTo(UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void fullUpdateUmurimoWithPatch() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();

        // Update the umurimo using partial update
        Umurimo partialUpdatedUmurimo = new Umurimo();
        partialUpdatedUmurimo.setId(umurimo.getId());

        partialUpdatedUmurimo.umurimo(UPDATED_UMURIMO).urwego(UPDATED_URWEGO);

        restUmurimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUmurimo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUmurimo))
            )
            .andExpect(status().isOk());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
        Umurimo testUmurimo = umurimoList.get(umurimoList.size() - 1);
        assertThat(testUmurimo.getUmurimo()).isEqualTo(UPDATED_UMURIMO);
        assertThat(testUmurimo.getUrwego()).isEqualTo(UPDATED_URWEGO);
    }

    @Test
    @Transactional
    void patchNonExistingUmurimo() throws Exception {
        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();
        umurimo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUmurimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, umurimo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(umurimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUmurimo() throws Exception {
        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();
        umurimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmurimoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(umurimo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUmurimo() throws Exception {
        int databaseSizeBeforeUpdate = umurimoRepository.findAll().size();
        umurimo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUmurimoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(umurimo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Umurimo in the database
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUmurimo() throws Exception {
        // Initialize the database
        umurimoRepository.saveAndFlush(umurimo);

        int databaseSizeBeforeDelete = umurimoRepository.findAll().size();

        // Delete the umurimo
        restUmurimoMockMvc
            .perform(delete(ENTITY_API_URL_ID, umurimo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Umurimo> umurimoList = umurimoRepository.findAll();
        assertThat(umurimoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
