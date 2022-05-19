package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.IcrfStatus;
import com.mycompany.myapp.repository.IcrfStatusRepository;
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
 * Integration tests for the {@link IcrfStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IcrfStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/icrf-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IcrfStatusRepository icrfStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIcrfStatusMockMvc;

    private IcrfStatus icrfStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IcrfStatus createEntity(EntityManager em) {
        IcrfStatus icrfStatus = new IcrfStatus().name(DEFAULT_NAME);
        return icrfStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IcrfStatus createUpdatedEntity(EntityManager em) {
        IcrfStatus icrfStatus = new IcrfStatus().name(UPDATED_NAME);
        return icrfStatus;
    }

    @BeforeEach
    public void initTest() {
        icrfStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createIcrfStatus() throws Exception {
        int databaseSizeBeforeCreate = icrfStatusRepository.findAll().size();
        // Create the IcrfStatus
        restIcrfStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrfStatus)))
            .andExpect(status().isCreated());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeCreate + 1);
        IcrfStatus testIcrfStatus = icrfStatusList.get(icrfStatusList.size() - 1);
        assertThat(testIcrfStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createIcrfStatusWithExistingId() throws Exception {
        // Create the IcrfStatus with an existing ID
        icrfStatus.setId(1L);

        int databaseSizeBeforeCreate = icrfStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIcrfStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrfStatus)))
            .andExpect(status().isBadRequest());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIcrfStatuses() throws Exception {
        // Initialize the database
        icrfStatusRepository.saveAndFlush(icrfStatus);

        // Get all the icrfStatusList
        restIcrfStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icrfStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getIcrfStatus() throws Exception {
        // Initialize the database
        icrfStatusRepository.saveAndFlush(icrfStatus);

        // Get the icrfStatus
        restIcrfStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, icrfStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(icrfStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIcrfStatus() throws Exception {
        // Get the icrfStatus
        restIcrfStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIcrfStatus() throws Exception {
        // Initialize the database
        icrfStatusRepository.saveAndFlush(icrfStatus);

        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();

        // Update the icrfStatus
        IcrfStatus updatedIcrfStatus = icrfStatusRepository.findById(icrfStatus.getId()).get();
        // Disconnect from session so that the updates on updatedIcrfStatus are not directly saved in db
        em.detach(updatedIcrfStatus);
        updatedIcrfStatus.name(UPDATED_NAME);

        restIcrfStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIcrfStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIcrfStatus))
            )
            .andExpect(status().isOk());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
        IcrfStatus testIcrfStatus = icrfStatusList.get(icrfStatusList.size() - 1);
        assertThat(testIcrfStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingIcrfStatus() throws Exception {
        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();
        icrfStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcrfStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, icrfStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icrfStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIcrfStatus() throws Exception {
        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();
        icrfStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icrfStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIcrfStatus() throws Exception {
        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();
        icrfStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrfStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIcrfStatusWithPatch() throws Exception {
        // Initialize the database
        icrfStatusRepository.saveAndFlush(icrfStatus);

        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();

        // Update the icrfStatus using partial update
        IcrfStatus partialUpdatedIcrfStatus = new IcrfStatus();
        partialUpdatedIcrfStatus.setId(icrfStatus.getId());

        partialUpdatedIcrfStatus.name(UPDATED_NAME);

        restIcrfStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcrfStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcrfStatus))
            )
            .andExpect(status().isOk());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
        IcrfStatus testIcrfStatus = icrfStatusList.get(icrfStatusList.size() - 1);
        assertThat(testIcrfStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateIcrfStatusWithPatch() throws Exception {
        // Initialize the database
        icrfStatusRepository.saveAndFlush(icrfStatus);

        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();

        // Update the icrfStatus using partial update
        IcrfStatus partialUpdatedIcrfStatus = new IcrfStatus();
        partialUpdatedIcrfStatus.setId(icrfStatus.getId());

        partialUpdatedIcrfStatus.name(UPDATED_NAME);

        restIcrfStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcrfStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcrfStatus))
            )
            .andExpect(status().isOk());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
        IcrfStatus testIcrfStatus = icrfStatusList.get(icrfStatusList.size() - 1);
        assertThat(testIcrfStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingIcrfStatus() throws Exception {
        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();
        icrfStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcrfStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, icrfStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icrfStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIcrfStatus() throws Exception {
        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();
        icrfStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icrfStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIcrfStatus() throws Exception {
        int databaseSizeBeforeUpdate = icrfStatusRepository.findAll().size();
        icrfStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(icrfStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IcrfStatus in the database
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIcrfStatus() throws Exception {
        // Initialize the database
        icrfStatusRepository.saveAndFlush(icrfStatus);

        int databaseSizeBeforeDelete = icrfStatusRepository.findAll().size();

        // Delete the icrfStatus
        restIcrfStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, icrfStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IcrfStatus> icrfStatusList = icrfStatusRepository.findAll();
        assertThat(icrfStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
