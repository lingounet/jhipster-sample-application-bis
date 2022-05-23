package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SensitiveData;
import com.mycompany.myapp.repository.SensitiveDataRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SensitiveDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensitiveDataResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sensitive-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensitiveDataRepository sensitiveDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensitiveDataMockMvc;

    private SensitiveData sensitiveData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensitiveData createEntity(EntityManager em) {
        SensitiveData sensitiveData = new SensitiveData().date(DEFAULT_DATE);
        return sensitiveData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensitiveData createUpdatedEntity(EntityManager em) {
        SensitiveData sensitiveData = new SensitiveData().date(UPDATED_DATE);
        return sensitiveData;
    }

    @BeforeEach
    public void initTest() {
        sensitiveData = createEntity(em);
    }

    @Test
    @Transactional
    void createSensitiveData() throws Exception {
        int databaseSizeBeforeCreate = sensitiveDataRepository.findAll().size();
        // Create the SensitiveData
        restSensitiveDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensitiveData)))
            .andExpect(status().isCreated());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeCreate + 1);
        SensitiveData testSensitiveData = sensitiveDataList.get(sensitiveDataList.size() - 1);
        assertThat(testSensitiveData.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createSensitiveDataWithExistingId() throws Exception {
        // Create the SensitiveData with an existing ID
        sensitiveData.setId(1L);

        int databaseSizeBeforeCreate = sensitiveDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensitiveDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensitiveData)))
            .andExpect(status().isBadRequest());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSensitiveData() throws Exception {
        // Initialize the database
        sensitiveDataRepository.saveAndFlush(sensitiveData);

        // Get all the sensitiveDataList
        restSensitiveDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensitiveData.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getSensitiveData() throws Exception {
        // Initialize the database
        sensitiveDataRepository.saveAndFlush(sensitiveData);

        // Get the sensitiveData
        restSensitiveDataMockMvc
            .perform(get(ENTITY_API_URL_ID, sensitiveData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensitiveData.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSensitiveData() throws Exception {
        // Get the sensitiveData
        restSensitiveDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensitiveData() throws Exception {
        // Initialize the database
        sensitiveDataRepository.saveAndFlush(sensitiveData);

        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();

        // Update the sensitiveData
        SensitiveData updatedSensitiveData = sensitiveDataRepository.findById(sensitiveData.getId()).get();
        // Disconnect from session so that the updates on updatedSensitiveData are not directly saved in db
        em.detach(updatedSensitiveData);
        updatedSensitiveData.date(UPDATED_DATE);

        restSensitiveDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSensitiveData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSensitiveData))
            )
            .andExpect(status().isOk());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
        SensitiveData testSensitiveData = sensitiveDataList.get(sensitiveDataList.size() - 1);
        assertThat(testSensitiveData.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSensitiveData() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();
        sensitiveData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensitiveDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensitiveData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensitiveData() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();
        sensitiveData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensitiveData() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();
        sensitiveData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sensitiveData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensitiveDataWithPatch() throws Exception {
        // Initialize the database
        sensitiveDataRepository.saveAndFlush(sensitiveData);

        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();

        // Update the sensitiveData using partial update
        SensitiveData partialUpdatedSensitiveData = new SensitiveData();
        partialUpdatedSensitiveData.setId(sensitiveData.getId());

        restSensitiveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensitiveData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensitiveData))
            )
            .andExpect(status().isOk());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
        SensitiveData testSensitiveData = sensitiveDataList.get(sensitiveDataList.size() - 1);
        assertThat(testSensitiveData.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSensitiveDataWithPatch() throws Exception {
        // Initialize the database
        sensitiveDataRepository.saveAndFlush(sensitiveData);

        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();

        // Update the sensitiveData using partial update
        SensitiveData partialUpdatedSensitiveData = new SensitiveData();
        partialUpdatedSensitiveData.setId(sensitiveData.getId());

        partialUpdatedSensitiveData.date(UPDATED_DATE);

        restSensitiveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensitiveData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensitiveData))
            )
            .andExpect(status().isOk());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
        SensitiveData testSensitiveData = sensitiveDataList.get(sensitiveDataList.size() - 1);
        assertThat(testSensitiveData.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSensitiveData() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();
        sensitiveData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensitiveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensitiveData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensitiveData() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();
        sensitiveData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensitiveData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensitiveData() throws Exception {
        int databaseSizeBeforeUpdate = sensitiveDataRepository.findAll().size();
        sensitiveData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensitiveDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sensitiveData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensitiveData in the database
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensitiveData() throws Exception {
        // Initialize the database
        sensitiveDataRepository.saveAndFlush(sensitiveData);

        int databaseSizeBeforeDelete = sensitiveDataRepository.findAll().size();

        // Delete the sensitiveData
        restSensitiveDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensitiveData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SensitiveData> sensitiveDataList = sensitiveDataRepository.findAll();
        assertThat(sensitiveDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
