package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Cell;
import com.minaloc.gov.domain.District;
import com.minaloc.gov.domain.Sector;
import com.minaloc.gov.repository.SectorRepository;
import com.minaloc.gov.service.SectorService;
import com.minaloc.gov.service.criteria.SectorCriteria;
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
 * Integration tests for the {@link SectorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SectorResourceIT {

    private static final String DEFAULT_SECTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sectors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SectorRepository sectorRepository;

    @Mock
    private SectorRepository sectorRepositoryMock;

    @Mock
    private SectorService sectorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectorMockMvc;

    private Sector sector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sector createEntity(EntityManager em) {
        Sector sector = new Sector().sectorCode(DEFAULT_SECTOR_CODE).name(DEFAULT_NAME);
        return sector;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sector createUpdatedEntity(EntityManager em) {
        Sector sector = new Sector().sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);
        return sector;
    }

    @BeforeEach
    public void initTest() {
        sector = createEntity(em);
    }

    @Test
    @Transactional
    void createSector() throws Exception {
        int databaseSizeBeforeCreate = sectorRepository.findAll().size();
        // Create the Sector
        restSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isCreated());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeCreate + 1);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getSectorCode()).isEqualTo(DEFAULT_SECTOR_CODE);
        assertThat(testSector.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSectorWithExistingId() throws Exception {
        // Create the Sector with an existing ID
        sector.setId(1L);

        int databaseSizeBeforeCreate = sectorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sectorRepository.findAll().size();
        // set the field null
        sector.setSectorCode(null);

        // Create the Sector, which fails.

        restSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isBadRequest());

        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sectorRepository.findAll().size();
        // set the field null
        sector.setName(null);

        // Create the Sector, which fails.

        restSectorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isBadRequest());

        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSectors() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList
        restSectorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sector.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorCode").value(hasItem(DEFAULT_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSectorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sectorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSectorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sectorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSectorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sectorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSectorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sectorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get the sector
        restSectorMockMvc
            .perform(get(ENTITY_API_URL_ID, sector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sector.getId().intValue()))
            .andExpect(jsonPath("$.sectorCode").value(DEFAULT_SECTOR_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getSectorsByIdFiltering() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        Long id = sector.getId();

        defaultSectorShouldBeFound("id.equals=" + id);
        defaultSectorShouldNotBeFound("id.notEquals=" + id);

        defaultSectorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSectorShouldNotBeFound("id.greaterThan=" + id);

        defaultSectorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSectorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSectorsBySectorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where sectorCode equals to DEFAULT_SECTOR_CODE
        defaultSectorShouldBeFound("sectorCode.equals=" + DEFAULT_SECTOR_CODE);

        // Get all the sectorList where sectorCode equals to UPDATED_SECTOR_CODE
        defaultSectorShouldNotBeFound("sectorCode.equals=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSectorsBySectorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where sectorCode not equals to DEFAULT_SECTOR_CODE
        defaultSectorShouldNotBeFound("sectorCode.notEquals=" + DEFAULT_SECTOR_CODE);

        // Get all the sectorList where sectorCode not equals to UPDATED_SECTOR_CODE
        defaultSectorShouldBeFound("sectorCode.notEquals=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSectorsBySectorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where sectorCode in DEFAULT_SECTOR_CODE or UPDATED_SECTOR_CODE
        defaultSectorShouldBeFound("sectorCode.in=" + DEFAULT_SECTOR_CODE + "," + UPDATED_SECTOR_CODE);

        // Get all the sectorList where sectorCode equals to UPDATED_SECTOR_CODE
        defaultSectorShouldNotBeFound("sectorCode.in=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSectorsBySectorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where sectorCode is not null
        defaultSectorShouldBeFound("sectorCode.specified=true");

        // Get all the sectorList where sectorCode is null
        defaultSectorShouldNotBeFound("sectorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSectorsBySectorCodeContainsSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where sectorCode contains DEFAULT_SECTOR_CODE
        defaultSectorShouldBeFound("sectorCode.contains=" + DEFAULT_SECTOR_CODE);

        // Get all the sectorList where sectorCode contains UPDATED_SECTOR_CODE
        defaultSectorShouldNotBeFound("sectorCode.contains=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSectorsBySectorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where sectorCode does not contain DEFAULT_SECTOR_CODE
        defaultSectorShouldNotBeFound("sectorCode.doesNotContain=" + DEFAULT_SECTOR_CODE);

        // Get all the sectorList where sectorCode does not contain UPDATED_SECTOR_CODE
        defaultSectorShouldBeFound("sectorCode.doesNotContain=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSectorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name equals to DEFAULT_NAME
        defaultSectorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sectorList where name equals to UPDATED_NAME
        defaultSectorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name not equals to DEFAULT_NAME
        defaultSectorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the sectorList where name not equals to UPDATED_NAME
        defaultSectorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSectorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sectorList where name equals to UPDATED_NAME
        defaultSectorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name is not null
        defaultSectorShouldBeFound("name.specified=true");

        // Get all the sectorList where name is null
        defaultSectorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSectorsByNameContainsSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name contains DEFAULT_NAME
        defaultSectorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sectorList where name contains UPDATED_NAME
        defaultSectorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name does not contain DEFAULT_NAME
        defaultSectorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sectorList where name does not contain UPDATED_NAME
        defaultSectorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSectorsByCellIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);
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
        sector.addCell(cell);
        sectorRepository.saveAndFlush(sector);
        Long cellId = cell.getId();

        // Get all the sectorList where cell equals to cellId
        defaultSectorShouldBeFound("cellId.equals=" + cellId);

        // Get all the sectorList where cell equals to (cellId + 1)
        defaultSectorShouldNotBeFound("cellId.equals=" + (cellId + 1));
    }

    @Test
    @Transactional
    void getAllSectorsByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        sector.setDistrict(district);
        sectorRepository.saveAndFlush(sector);
        Long districtId = district.getId();

        // Get all the sectorList where district equals to districtId
        defaultSectorShouldBeFound("districtId.equals=" + districtId);

        // Get all the sectorList where district equals to (districtId + 1)
        defaultSectorShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSectorShouldBeFound(String filter) throws Exception {
        restSectorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sector.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorCode").value(hasItem(DEFAULT_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restSectorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSectorShouldNotBeFound(String filter) throws Exception {
        restSectorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSector() throws Exception {
        // Get the sector
        restSectorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Update the sector
        Sector updatedSector = sectorRepository.findById(sector.getId()).get();
        // Disconnect from session so that the updates on updatedSector are not directly saved in db
        em.detach(updatedSector);
        updatedSector.sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);

        restSectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSector.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSector))
            )
            .andExpect(status().isOk());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getSectorCode()).isEqualTo(UPDATED_SECTOR_CODE);
        assertThat(testSector.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();
        sector.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sector.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sector))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();
        sector.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sector))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();
        sector.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSectorWithPatch() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Update the sector using partial update
        Sector partialUpdatedSector = new Sector();
        partialUpdatedSector.setId(sector.getId());

        partialUpdatedSector.sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);

        restSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSector.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSector))
            )
            .andExpect(status().isOk());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getSectorCode()).isEqualTo(UPDATED_SECTOR_CODE);
        assertThat(testSector.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSectorWithPatch() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Update the sector using partial update
        Sector partialUpdatedSector = new Sector();
        partialUpdatedSector.setId(sector.getId());

        partialUpdatedSector.sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);

        restSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSector.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSector))
            )
            .andExpect(status().isOk());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getSectorCode()).isEqualTo(UPDATED_SECTOR_CODE);
        assertThat(testSector.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();
        sector.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sector.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sector))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();
        sector.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sector))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();
        sector.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sector)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeDelete = sectorRepository.findAll().size();

        // Delete the sector
        restSectorMockMvc
            .perform(delete(ENTITY_API_URL_ID, sector.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
