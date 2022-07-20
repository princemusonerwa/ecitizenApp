package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Cell;
import com.minaloc.gov.domain.Sector;
import com.minaloc.gov.domain.Village;
import com.minaloc.gov.repository.CellRepository;
import com.minaloc.gov.service.CellService;
import com.minaloc.gov.service.criteria.CellCriteria;
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
 * Integration tests for the {@link CellResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CellResourceIT {

    private static final String DEFAULT_SECTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cells";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CellRepository cellRepository;

    @Mock
    private CellRepository cellRepositoryMock;

    @Mock
    private CellService cellServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCellMockMvc;

    private Cell cell;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cell createEntity(EntityManager em) {
        Cell cell = new Cell().sectorCode(DEFAULT_SECTOR_CODE).name(DEFAULT_NAME);
        return cell;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cell createUpdatedEntity(EntityManager em) {
        Cell cell = new Cell().sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);
        return cell;
    }

    @BeforeEach
    public void initTest() {
        cell = createEntity(em);
    }

    @Test
    @Transactional
    void createCell() throws Exception {
        int databaseSizeBeforeCreate = cellRepository.findAll().size();
        // Create the Cell
        restCellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cell)))
            .andExpect(status().isCreated());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeCreate + 1);
        Cell testCell = cellList.get(cellList.size() - 1);
        assertThat(testCell.getSectorCode()).isEqualTo(DEFAULT_SECTOR_CODE);
        assertThat(testCell.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCellWithExistingId() throws Exception {
        // Create the Cell with an existing ID
        cell.setId(1L);

        int databaseSizeBeforeCreate = cellRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cell)))
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cellRepository.findAll().size();
        // set the field null
        cell.setSectorCode(null);

        // Create the Cell, which fails.

        restCellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cell)))
            .andExpect(status().isBadRequest());

        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cellRepository.findAll().size();
        // set the field null
        cell.setName(null);

        // Create the Cell, which fails.

        restCellMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cell)))
            .andExpect(status().isBadRequest());

        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCells() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList
        restCellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cell.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorCode").value(hasItem(DEFAULT_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCellsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cellServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCellMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cellServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCellsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cellServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCellMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cellServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get the cell
        restCellMockMvc
            .perform(get(ENTITY_API_URL_ID, cell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cell.getId().intValue()))
            .andExpect(jsonPath("$.sectorCode").value(DEFAULT_SECTOR_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCellsByIdFiltering() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        Long id = cell.getId();

        defaultCellShouldBeFound("id.equals=" + id);
        defaultCellShouldNotBeFound("id.notEquals=" + id);

        defaultCellShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCellShouldNotBeFound("id.greaterThan=" + id);

        defaultCellShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCellShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCellsBySectorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where sectorCode equals to DEFAULT_SECTOR_CODE
        defaultCellShouldBeFound("sectorCode.equals=" + DEFAULT_SECTOR_CODE);

        // Get all the cellList where sectorCode equals to UPDATED_SECTOR_CODE
        defaultCellShouldNotBeFound("sectorCode.equals=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllCellsBySectorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where sectorCode not equals to DEFAULT_SECTOR_CODE
        defaultCellShouldNotBeFound("sectorCode.notEquals=" + DEFAULT_SECTOR_CODE);

        // Get all the cellList where sectorCode not equals to UPDATED_SECTOR_CODE
        defaultCellShouldBeFound("sectorCode.notEquals=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllCellsBySectorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where sectorCode in DEFAULT_SECTOR_CODE or UPDATED_SECTOR_CODE
        defaultCellShouldBeFound("sectorCode.in=" + DEFAULT_SECTOR_CODE + "," + UPDATED_SECTOR_CODE);

        // Get all the cellList where sectorCode equals to UPDATED_SECTOR_CODE
        defaultCellShouldNotBeFound("sectorCode.in=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllCellsBySectorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where sectorCode is not null
        defaultCellShouldBeFound("sectorCode.specified=true");

        // Get all the cellList where sectorCode is null
        defaultCellShouldNotBeFound("sectorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCellsBySectorCodeContainsSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where sectorCode contains DEFAULT_SECTOR_CODE
        defaultCellShouldBeFound("sectorCode.contains=" + DEFAULT_SECTOR_CODE);

        // Get all the cellList where sectorCode contains UPDATED_SECTOR_CODE
        defaultCellShouldNotBeFound("sectorCode.contains=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllCellsBySectorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where sectorCode does not contain DEFAULT_SECTOR_CODE
        defaultCellShouldNotBeFound("sectorCode.doesNotContain=" + DEFAULT_SECTOR_CODE);

        // Get all the cellList where sectorCode does not contain UPDATED_SECTOR_CODE
        defaultCellShouldBeFound("sectorCode.doesNotContain=" + UPDATED_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllCellsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where name equals to DEFAULT_NAME
        defaultCellShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cellList where name equals to UPDATED_NAME
        defaultCellShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCellsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where name not equals to DEFAULT_NAME
        defaultCellShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the cellList where name not equals to UPDATED_NAME
        defaultCellShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCellsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCellShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cellList where name equals to UPDATED_NAME
        defaultCellShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCellsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where name is not null
        defaultCellShouldBeFound("name.specified=true");

        // Get all the cellList where name is null
        defaultCellShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCellsByNameContainsSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where name contains DEFAULT_NAME
        defaultCellShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cellList where name contains UPDATED_NAME
        defaultCellShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCellsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cellList where name does not contain DEFAULT_NAME
        defaultCellShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cellList where name does not contain UPDATED_NAME
        defaultCellShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCellsByVillageIsEqualToSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
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
        cell.addVillage(village);
        cellRepository.saveAndFlush(cell);
        Long villageId = village.getId();

        // Get all the cellList where village equals to villageId
        defaultCellShouldBeFound("villageId.equals=" + villageId);

        // Get all the cellList where village equals to (villageId + 1)
        defaultCellShouldNotBeFound("villageId.equals=" + (villageId + 1));
    }

    @Test
    @Transactional
    void getAllCellsBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
        Sector sector;
        if (TestUtil.findAll(em, Sector.class).isEmpty()) {
            sector = SectorResourceIT.createEntity(em);
            em.persist(sector);
            em.flush();
        } else {
            sector = TestUtil.findAll(em, Sector.class).get(0);
        }
        em.persist(sector);
        em.flush();
        cell.setSector(sector);
        cellRepository.saveAndFlush(cell);
        Long sectorId = sector.getId();

        // Get all the cellList where sector equals to sectorId
        defaultCellShouldBeFound("sectorId.equals=" + sectorId);

        // Get all the cellList where sector equals to (sectorId + 1)
        defaultCellShouldNotBeFound("sectorId.equals=" + (sectorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCellShouldBeFound(String filter) throws Exception {
        restCellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cell.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorCode").value(hasItem(DEFAULT_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCellMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCellShouldNotBeFound(String filter) throws Exception {
        restCellMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCellMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCell() throws Exception {
        // Get the cell
        restCellMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        int databaseSizeBeforeUpdate = cellRepository.findAll().size();

        // Update the cell
        Cell updatedCell = cellRepository.findById(cell.getId()).get();
        // Disconnect from session so that the updates on updatedCell are not directly saved in db
        em.detach(updatedCell);
        updatedCell.sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);

        restCellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCell))
            )
            .andExpect(status().isOk());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
        Cell testCell = cellList.get(cellList.size() - 1);
        assertThat(testCell.getSectorCode()).isEqualTo(UPDATED_SECTOR_CODE);
        assertThat(testCell.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();
        cell.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cell.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();
        cell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();
        cell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCellWithPatch() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        int databaseSizeBeforeUpdate = cellRepository.findAll().size();

        // Update the cell using partial update
        Cell partialUpdatedCell = new Cell();
        partialUpdatedCell.setId(cell.getId());

        partialUpdatedCell.sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);

        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCell.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCell))
            )
            .andExpect(status().isOk());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
        Cell testCell = cellList.get(cellList.size() - 1);
        assertThat(testCell.getSectorCode()).isEqualTo(UPDATED_SECTOR_CODE);
        assertThat(testCell.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCellWithPatch() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        int databaseSizeBeforeUpdate = cellRepository.findAll().size();

        // Update the cell using partial update
        Cell partialUpdatedCell = new Cell();
        partialUpdatedCell.setId(cell.getId());

        partialUpdatedCell.sectorCode(UPDATED_SECTOR_CODE).name(UPDATED_NAME);

        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCell.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCell))
            )
            .andExpect(status().isOk());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
        Cell testCell = cellList.get(cellList.size() - 1);
        assertThat(testCell.getSectorCode()).isEqualTo(UPDATED_SECTOR_CODE);
        assertThat(testCell.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();
        cell.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cell.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();
        cell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cell))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCell() throws Exception {
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();
        cell.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCellMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cell)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cell in the database
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        int databaseSizeBeforeDelete = cellRepository.findAll().size();

        // Delete the cell
        restCellMockMvc
            .perform(delete(ENTITY_API_URL_ID, cell.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cell> cellList = cellRepository.findAll();
        assertThat(cellList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
