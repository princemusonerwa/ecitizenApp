package com.minaloc.gov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minaloc.gov.IntegrationTest;
import com.minaloc.gov.domain.Office;
import com.minaloc.gov.domain.enumeration.OfficeType;
import com.minaloc.gov.repository.OfficeRepository;
import com.minaloc.gov.service.OfficeService;
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
 * Integration tests for the {@link OfficeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OfficeResourceIT {

    private static final OfficeType DEFAULT_OFFICE_TYPE = OfficeType.MINALOC;
    private static final OfficeType UPDATED_OFFICE_TYPE = OfficeType.PROVINCE;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/offices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfficeRepository officeRepository;

    @Mock
    private OfficeRepository officeRepositoryMock;

    @Mock
    private OfficeService officeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfficeMockMvc;

    private Office office;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Office createEntity(EntityManager em) {
        Office office = new Office().officeType(DEFAULT_OFFICE_TYPE).name(DEFAULT_NAME).createdAt(DEFAULT_CREATED_AT);
        return office;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Office createUpdatedEntity(EntityManager em) {
        Office office = new Office().officeType(UPDATED_OFFICE_TYPE).name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);
        return office;
    }

    @BeforeEach
    public void initTest() {
        office = createEntity(em);
    }

    @Test
    @Transactional
    void createOffice() throws Exception {
        int databaseSizeBeforeCreate = officeRepository.findAll().size();
        // Create the Office
        restOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isCreated());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeCreate + 1);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getOfficeType()).isEqualTo(DEFAULT_OFFICE_TYPE);
        assertThat(testOffice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOffice.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createOfficeWithExistingId() throws Exception {
        // Create the Office with an existing ID
        office.setId(1L);

        int databaseSizeBeforeCreate = officeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isBadRequest());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOfficeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = officeRepository.findAll().size();
        // set the field null
        office.setOfficeType(null);

        // Create the Office, which fails.

        restOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isBadRequest());

        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = officeRepository.findAll().size();
        // set the field null
        office.setName(null);

        // Create the Office, which fails.

        restOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isBadRequest());

        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = officeRepository.findAll().size();
        // set the field null
        office.setCreatedAt(null);

        // Create the Office, which fails.

        restOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isBadRequest());

        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOffices() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        // Get all the officeList
        restOfficeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(office.getId().intValue())))
            .andExpect(jsonPath("$.[*].officeType").value(hasItem(DEFAULT_OFFICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        // Get the office
        restOfficeMockMvc
            .perform(get(ENTITY_API_URL_ID, office.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(office.getId().intValue()))
            .andExpect(jsonPath("$.officeType").value(DEFAULT_OFFICE_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOffice() throws Exception {
        // Get the office
        restOfficeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Update the office
        Office updatedOffice = officeRepository.findById(office.getId()).get();
        // Disconnect from session so that the updates on updatedOffice are not directly saved in db
        em.detach(updatedOffice);
        updatedOffice.officeType(UPDATED_OFFICE_TYPE).name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        restOfficeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOffice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOffice))
            )
            .andExpect(status().isOk());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getOfficeType()).isEqualTo(UPDATED_OFFICE_TYPE);
        assertThat(testOffice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOffice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();
        office.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfficeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, office.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(office))
            )
            .andExpect(status().isBadRequest());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();
        office.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(office))
            )
            .andExpect(status().isBadRequest());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();
        office.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfficeWithPatch() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Update the office using partial update
        Office partialUpdatedOffice = new Office();
        partialUpdatedOffice.setId(office.getId());

        partialUpdatedOffice.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        restOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffice))
            )
            .andExpect(status().isOk());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getOfficeType()).isEqualTo(DEFAULT_OFFICE_TYPE);
        assertThat(testOffice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOffice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOfficeWithPatch() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Update the office using partial update
        Office partialUpdatedOffice = new Office();
        partialUpdatedOffice.setId(office.getId());

        partialUpdatedOffice.officeType(UPDATED_OFFICE_TYPE).name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT);

        restOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffice))
            )
            .andExpect(status().isOk());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getOfficeType()).isEqualTo(UPDATED_OFFICE_TYPE);
        assertThat(testOffice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOffice.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();
        office.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, office.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(office))
            )
            .andExpect(status().isBadRequest());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();
        office.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(office))
            )
            .andExpect(status().isBadRequest());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();
        office.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfficeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(office)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        int databaseSizeBeforeDelete = officeRepository.findAll().size();

        // Delete the office
        restOfficeMockMvc
            .perform(delete(ENTITY_API_URL_ID, office.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
