package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.District;
import com.minaloc.gov.domain.Province;
import com.minaloc.gov.domain.Sector;
import com.minaloc.gov.repository.DistrictRepository;
import com.minaloc.gov.service.DistrictService;
import com.minaloc.gov.service.criteria.DistrictCriteria;
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
 * Integration tests for the {@link DistrictResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DistrictResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/districts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DistrictRepository districtRepository;

    @Mock
    private DistrictRepository districtRepositoryMock;

    @Mock
    private DistrictService districtServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistrictMockMvc;

    private District district;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static District createEntity(EntityManager em) {
        District district = new District().name(DEFAULT_NAME);
        return district;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static District createUpdatedEntity(EntityManager em) {
        District district = new District().name(UPDATED_NAME);
        return district;
    }

    @BeforeEach
    public void initTest() {
        district = createEntity(em);
    }

    @Test
    @Transactional
    void createDistrict() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();
        // Create the District
        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isCreated());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate + 1);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDistrictWithExistingId() throws Exception {
        // Create the District with an existing ID
        district.setId(1L);

        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setName(null);

        // Create the District, which fails.

        restDistrictMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isBadRequest());

        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDistricts() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDistrictsWithEagerRelationshipsIsEnabled() throws Exception {
        when(districtServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDistrictMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(districtServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDistrictsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(districtServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDistrictMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(districtServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get the district
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL_ID, district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(district.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getDistrictsByIdFiltering() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        Long id = district.getId();

        defaultDistrictShouldBeFound("id.equals=" + id);
        defaultDistrictShouldNotBeFound("id.notEquals=" + id);

        defaultDistrictShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDistrictShouldNotBeFound("id.greaterThan=" + id);

        defaultDistrictShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDistrictShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDistrictsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where name equals to DEFAULT_NAME
        defaultDistrictShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the districtList where name equals to UPDATED_NAME
        defaultDistrictShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where name not equals to DEFAULT_NAME
        defaultDistrictShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the districtList where name not equals to UPDATED_NAME
        defaultDistrictShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDistrictShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the districtList where name equals to UPDATED_NAME
        defaultDistrictShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where name is not null
        defaultDistrictShouldBeFound("name.specified=true");

        // Get all the districtList where name is null
        defaultDistrictShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDistrictsByNameContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where name contains DEFAULT_NAME
        defaultDistrictShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the districtList where name contains UPDATED_NAME
        defaultDistrictShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districtList where name does not contain DEFAULT_NAME
        defaultDistrictShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the districtList where name does not contain UPDATED_NAME
        defaultDistrictShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDistrictsBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);
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
        district.addSector(sector);
        districtRepository.saveAndFlush(district);
        Long sectorId = sector.getId();

        // Get all the districtList where sector equals to sectorId
        defaultDistrictShouldBeFound("sectorId.equals=" + sectorId);

        // Get all the districtList where sector equals to (sectorId + 1)
        defaultDistrictShouldNotBeFound("sectorId.equals=" + (sectorId + 1));
    }

    @Test
    @Transactional
    void getAllDistrictsByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);
        Province province;
        if (TestUtil.findAll(em, Province.class).isEmpty()) {
            province = ProvinceResourceIT.createEntity(em);
            em.persist(province);
            em.flush();
        } else {
            province = TestUtil.findAll(em, Province.class).get(0);
        }
        em.persist(province);
        em.flush();
        district.setProvince(province);
        districtRepository.saveAndFlush(district);
        Long provinceId = province.getId();

        // Get all the districtList where province equals to provinceId
        defaultDistrictShouldBeFound("provinceId.equals=" + provinceId);

        // Get all the districtList where province equals to (provinceId + 1)
        defaultDistrictShouldNotBeFound("provinceId.equals=" + (provinceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDistrictShouldBeFound(String filter) throws Exception {
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDistrictShouldNotBeFound(String filter) throws Exception {
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDistrictMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDistrict() throws Exception {
        // Get the district
        restDistrictMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district
        District updatedDistrict = districtRepository.findById(district.getId()).get();
        // Disconnect from session so that the updates on updatedDistrict are not directly saved in db
        em.detach(updatedDistrict);
        updatedDistrict.name(UPDATED_NAME);

        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDistrict.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, district.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(district))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(district))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDistrictWithPatch() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district using partial update
        District partialUpdatedDistrict = new District();
        partialUpdatedDistrict.setId(district.getId());

        partialUpdatedDistrict.name(UPDATED_NAME);

        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDistrictWithPatch() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district using partial update
        District partialUpdatedDistrict = new District();
        partialUpdatedDistrict.setId(district.getId());

        partialUpdatedDistrict.name(UPDATED_NAME);

        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistrict.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDistrict))
            )
            .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districtList.get(districtList.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, district.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(district))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(district))
            )
            .andExpect(status().isBadRequest());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistrict() throws Exception {
        int databaseSizeBeforeUpdate = districtRepository.findAll().size();
        district.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistrictMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the District in the database
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        int databaseSizeBeforeDelete = districtRepository.findAll().size();

        // Delete the district
        restDistrictMockMvc
            .perform(delete(ENTITY_API_URL_ID, district.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<District> districtList = districtRepository.findAll();
        assertThat(districtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
