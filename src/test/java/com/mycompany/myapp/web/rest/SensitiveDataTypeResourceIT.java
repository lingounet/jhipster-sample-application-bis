package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SensitiveDataType;
import com.mycompany.myapp.repository.SensitiveDataTypeRepository;
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
 * Integration tests for the {@link SensitiveDataTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensitiveDataTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sensitive-data-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensitiveDataTypeRepository sensitiveDataTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensitiveDataTypeMockMvc;

    private SensitiveDataType sensitiveDataType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensitiveDataType createEntity(EntityManager em) {
        SensitiveDataType sensitiveDataType = new SensitiveDataType().name(DEFAULT_NAME);
        return sensitiveDataType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensitiveDataType createUpdatedEntity(EntityManager em) {
        SensitiveDataType sensitiveDataType = new SensitiveDataType().name(UPDATED_NAME);
        return sensitiveDataType;
    }

    @BeforeEach
    public void initTest() {
        sensitiveDataType = createEntity(em);
    }

    @Test
    @Transactional
    void createSensitiveDataType() throws Exception {
        int databaseSizeBeforeCreate = sensitiveDataTypeRepository.findAll().size();
        // Create the SensitiveDataType
        restSensitiveDataTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isCreated());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SensitiveDataType testSensitiveDataType = sensitiveDataTypeList.get(sensitiveDataTypeList.size() - 1);
        assertThat(testSensitiveDataType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSensitiveDataTypeWithExistingId() throws Exception {
        // Create the SensitiveDataType with an existing ID
        sensitiveDataType.setId(1L);

        int databaseSizeBeforeCreate = sensitiveDataTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensitiveDataTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSensitiveDataTypes() throws Exception {
        // Initialize the database
        sensitiveDataTypeRepository.saveAndFlush(sensitiveDataType);

        // Get all the sensitiveDataTypeList
        restSensitiveDataTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensitiveDataType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSensitiveDataType() throws Exception {
        // Initialize the database
        sensitiveDataTypeRepository.saveAndFlush(sensitiveDataType);

        // Get the sensitiveDataType
        restSensitiveDataTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, sensitiveDataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensitiveDataType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSensitiveDataType() throws Exception {
        // Get the sensitiveDataType
        restSensitiveDataTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensitiveDataType() throws Exception {
        // Initialize the database
        sensitiveDataTypeRepository.saveAndFlush(sensitiveDataType);

        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();

        // Update the sensitiveDataType
        SensitiveDataType updatedSensitiveDataType = sensitiveDataTypeRepository.findById(sensitiveDataType.getId()).get();
        // Disconnect from session so that the updates on updatedSensitiveDataType are not directly saved in db
        em.detach(updatedSensitiveDataType);
        updatedSensitiveDataType.name(UPDATED_NAME);

        restSensitiveDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSensitiveDataType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSensitiveDataType))
            )
            .andExpect(status().isOk());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
        SensitiveDataType testSensitiveDataType = sensitiveDataTypeList.get(sensitiveDataTypeList.size() - 1);
        assertThat(testSensitiveDataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSensitiveDataType() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();
        sensitiveDataType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensitiveDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensitiveDataType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensitiveDataType() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();
        sensitiveDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensitiveDataType() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();
        sensitiveDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensitiveDataTypeWithPatch() throws Exception {
        // Initialize the database
        sensitiveDataTypeRepository.saveAndFlush(sensitiveDataType);

        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();

        // Update the sensitiveDataType using partial update
        SensitiveDataType partialUpdatedSensitiveDataType = new SensitiveDataType();
        partialUpdatedSensitiveDataType.setId(sensitiveDataType.getId());

        partialUpdatedSensitiveDataType.name(UPDATED_NAME);

        restSensitiveDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensitiveDataType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensitiveDataType))
            )
            .andExpect(status().isOk());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
        SensitiveDataType testSensitiveDataType = sensitiveDataTypeList.get(sensitiveDataTypeList.size() - 1);
        assertThat(testSensitiveDataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSensitiveDataTypeWithPatch() throws Exception {
        // Initialize the database
        sensitiveDataTypeRepository.saveAndFlush(sensitiveDataType);

        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();

        // Update the sensitiveDataType using partial update
        SensitiveDataType partialUpdatedSensitiveDataType = new SensitiveDataType();
        partialUpdatedSensitiveDataType.setId(sensitiveDataType.getId());

        partialUpdatedSensitiveDataType.name(UPDATED_NAME);

        restSensitiveDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensitiveDataType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensitiveDataType))
            )
            .andExpect(status().isOk());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
        SensitiveDataType testSensitiveDataType = sensitiveDataTypeList.get(sensitiveDataTypeList.size() - 1);
        assertThat(testSensitiveDataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSensitiveDataType() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();
        sensitiveDataType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensitiveDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensitiveDataType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensitiveDataType() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();
        sensitiveDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensitiveDataType() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataTypeRepository.findAll().size();
        sensitiveDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveDataType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensitiveDataType in the database
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensitiveDataType() throws Exception {
        // Initialize the database
        sensitiveDataTypeRepository.saveAndFlush(sensitiveDataType);

        int databaseSizeBeforeDelete = sensitiveDataTypeRepository.findAll().size();

        // Delete the sensitiveDataType
        restSensitiveDataTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensitiveDataType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SensitiveDataType> sensitiveDataTypeList = sensitiveDataTypeRepository.findAll();
        assertThat(sensitiveDataTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
