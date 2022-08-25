package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Cell;
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.domain.Village;
import com.minaloc.gov.repository.VillageRepository;
import com.minaloc.gov.service.VillageService;
import com.minaloc.gov.service.criteria.VillageCriteria;
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
 * Integration tests for the {@link VillageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VillageResourceIT {

    private static final String DEFAULT_VILLAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VILLAGE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/villages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VillageRepository villageRepository;

    @Mock
    private VillageRepository villageRepositoryMock;

    @Mock
    private VillageService villageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVillageMockMvc;

    private Village village;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createEntity(EntityManager em) {
        Village village = new Village().villageCode(DEFAULT_VILLAGE_CODE).name(DEFAULT_NAME);
        return village;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Village createUpdatedEntity(EntityManager em) {
        Village village = new Village().villageCode(UPDATED_VILLAGE_CODE).name(UPDATED_NAME);
        return village;
    }

    @BeforeEach
    public void initTest() {
        village = createEntity(em);
    }

    @Test
    @Transactional
    void createVillage() throws Exception {
        int databaseSizeBeforeCreate = villageRepository.findAll().size();
        // Create the Village
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(village)))
            .andExpect(status().isCreated());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate + 1);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageCode()).isEqualTo(DEFAULT_VILLAGE_CODE);
        assertThat(testVillage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createVillageWithExistingId() throws Exception {
        // Create the Village with an existing ID
        village.setId(1L);

        int databaseSizeBeforeCreate = villageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(village)))
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVillageCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = villageRepository.findAll().size();
        // set the field null
        village.setVillageCode(null);

        // Create the Village, which fails.

        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(village)))
            .andExpect(status().isBadRequest());

        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = villageRepository.findAll().size();
        // set the field null
        village.setName(null);

        // Create the Village, which fails.

        restVillageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(village)))
            .andExpect(status().isBadRequest());

        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVillages() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].villageCode").value(hasItem(DEFAULT_VILLAGE_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVillagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(villageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVillageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(villageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVillagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(villageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVillageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(villageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get the village
        restVillageMockMvc
            .perform(get(ENTITY_API_URL_ID, village.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(village.getId().intValue()))
            .andExpect(jsonPath("$.villageCode").value(DEFAULT_VILLAGE_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getVillagesByIdFiltering() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        Long id = village.getId();

        defaultVillageShouldBeFound("id.equals=" + id);
        defaultVillageShouldNotBeFound("id.notEquals=" + id);

        defaultVillageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVillageShouldNotBeFound("id.greaterThan=" + id);

        defaultVillageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVillageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode equals to DEFAULT_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.equals=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode equals to UPDATED_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.equals=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode not equals to DEFAULT_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.notEquals=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode not equals to UPDATED_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.notEquals=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode in DEFAULT_VILLAGE_CODE or UPDATED_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.in=" + DEFAULT_VILLAGE_CODE + "," + UPDATED_VILLAGE_CODE);

        // Get all the villageList where villageCode equals to UPDATED_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.in=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode is not null
        defaultVillageShouldBeFound("villageCode.specified=true");

        // Get all the villageList where villageCode is null
        defaultVillageShouldNotBeFound("villageCode.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode contains DEFAULT_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.contains=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode contains UPDATED_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.contains=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByVillageCodeNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where villageCode does not contain DEFAULT_VILLAGE_CODE
        defaultVillageShouldNotBeFound("villageCode.doesNotContain=" + DEFAULT_VILLAGE_CODE);

        // Get all the villageList where villageCode does not contain UPDATED_VILLAGE_CODE
        defaultVillageShouldBeFound("villageCode.doesNotContain=" + UPDATED_VILLAGE_CODE);
    }

    @Test
    @Transactional
    void getAllVillagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where name equals to DEFAULT_NAME
        defaultVillageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the villageList where name equals to UPDATED_NAME
        defaultVillageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where name not equals to DEFAULT_NAME
        defaultVillageShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the villageList where name not equals to UPDATED_NAME
        defaultVillageShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVillageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the villageList where name equals to UPDATED_NAME
        defaultVillageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where name is not null
        defaultVillageShouldBeFound("name.specified=true");

        // Get all the villageList where name is null
        defaultVillageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVillagesByNameContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where name contains DEFAULT_NAME
        defaultVillageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the villageList where name contains UPDATED_NAME
        defaultVillageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        // Get all the villageList where name does not contain DEFAULT_NAME
        defaultVillageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the villageList where name does not contain UPDATED_NAME
        defaultVillageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVillagesByUmuturageIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);
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
        village.addUmuturage(umuturage);
        villageRepository.saveAndFlush(village);
        Long umuturageId = umuturage.getId();

        // Get all the villageList where umuturage equals to umuturageId
        defaultVillageShouldBeFound("umuturageId.equals=" + umuturageId);

        // Get all the villageList where umuturage equals to (umuturageId + 1)
        defaultVillageShouldNotBeFound("umuturageId.equals=" + (umuturageId + 1));
    }

    @Test
    @Transactional
    void getAllVillagesByCellIsEqualToSomething() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);
        Cell cell;
        if (TestUtil.findAll(em, Cell.class).isEmpty()) {
            cell = CellResourceIT.createEntity(em);
            em.persist(cell);
            em.flush();
        } else {
            cell = TestUtil.findAll(em, Cell.class).get(0);
        }
        em.persist(cell);
        em.flush();
        village.setCell(cell);
        villageRepository.saveAndFlush(village);
        Long cellId = cell.getId();

        // Get all the villageList where cell equals to cellId
        defaultVillageShouldBeFound("cellId.equals=" + cellId);

        // Get all the villageList where cell equals to (cellId + 1)
        defaultVillageShouldNotBeFound("cellId.equals=" + (cellId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVillageShouldBeFound(String filter) throws Exception {
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(village.getId().intValue())))
            .andExpect(jsonPath("$.[*].villageCode").value(hasItem(DEFAULT_VILLAGE_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVillageShouldNotBeFound(String filter) throws Exception {
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVillage() throws Exception {
        // Get the village
        restVillageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village
        Village updatedVillage = villageRepository.findById(village.getId()).get();
        // Disconnect from session so that the updates on updatedVillage are not directly saved in db
        em.detach(updatedVillage);
        updatedVillage.villageCode(UPDATED_VILLAGE_CODE).name(UPDATED_NAME);

        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVillage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageCode()).isEqualTo(UPDATED_VILLAGE_CODE);
        assertThat(testVillage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, village.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(village))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(village))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(village)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage.villageCode(UPDATED_VILLAGE_CODE);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageCode()).isEqualTo(UPDATED_VILLAGE_CODE);
        assertThat(testVillage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVillageWithPatch() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeUpdate = villageRepository.findAll().size();

        // Update the village using partial update
        Village partialUpdatedVillage = new Village();
        partialUpdatedVillage.setId(village.getId());

        partialUpdatedVillage.villageCode(UPDATED_VILLAGE_CODE).name(UPDATED_NAME);

        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVillage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVillage))
            )
            .andExpect(status().isOk());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
        Village testVillage = villageList.get(villageList.size() - 1);
        assertThat(testVillage.getVillageCode()).isEqualTo(UPDATED_VILLAGE_CODE);
        assertThat(testVillage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, village.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(village))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(village))
            )
            .andExpect(status().isBadRequest());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVillage() throws Exception {
        int databaseSizeBeforeUpdate = villageRepository.findAll().size();
        village.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVillageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(village)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Village in the database
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVillage() throws Exception {
        // Initialize the database
        villageRepository.saveAndFlush(village);

        int databaseSizeBeforeDelete = villageRepository.findAll().size();

        // Delete the village
        restVillageMockMvc
            .perform(delete(ENTITY_API_URL_ID, village.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Village> villageList = villageRepository.findAll();
        assertThat(villageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
