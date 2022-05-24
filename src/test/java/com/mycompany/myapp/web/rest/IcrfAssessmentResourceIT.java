package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.IcrfAssessment;
import com.mycompany.myapp.repository.IcrfAssessmentRepository;
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
 * Integration tests for the {@link IcrfAssessmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IcrfAssessmentResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/icrf-assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IcrfAssessmentRepository icrfAssessmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIcrfAssessmentMockMvc;

    private IcrfAssessment icrfAssessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IcrfAssessment createEntity(EntityManager em) {
        IcrfAssessment icrfAssessment = new IcrfAssessment().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION).status(DEFAULT_STATUS);
        return icrfAssessment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IcrfAssessment createUpdatedEntity(EntityManager em) {
        IcrfAssessment icrfAssessment = new IcrfAssessment().code(UPDATED_CODE).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);
        return icrfAssessment;
    }

    @BeforeEach
    public void initTest() {
        icrfAssessment = createEntity(em);
    }

    @Test
    @Transactional
    void createIcrfAssessment() throws Exception {
        int databaseSizeBeforeCreate = icrfAssessmentRepository.findAll().size();
        // Create the IcrfAssessment
        restIcrfAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isCreated());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeCreate + 1);
        IcrfAssessment testIcrfAssessment = icrfAssessmentList.get(icrfAssessmentList.size() - 1);
        assertThat(testIcrfAssessment.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testIcrfAssessment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIcrfAssessment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createIcrfAssessmentWithExistingId() throws Exception {
        // Create the IcrfAssessment with an existing ID
        icrfAssessment.setId(1L);

        int databaseSizeBeforeCreate = icrfAssessmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIcrfAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIcrfAssessments() throws Exception {
        // Initialize the database
        icrfAssessmentRepository.saveAndFlush(icrfAssessment);

        // Get all the icrfAssessmentList
        restIcrfAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icrfAssessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getIcrfAssessment() throws Exception {
        // Initialize the database
        icrfAssessmentRepository.saveAndFlush(icrfAssessment);

        // Get the icrfAssessment
        restIcrfAssessmentMockMvc
            .perform(get(ENTITY_API_URL_ID, icrfAssessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(icrfAssessment.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingIcrfAssessment() throws Exception {
        // Get the icrfAssessment
        restIcrfAssessmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIcrfAssessment() throws Exception {
        // Initialize the database
        icrfAssessmentRepository.saveAndFlush(icrfAssessment);

        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();

        // Update the icrfAssessment
        IcrfAssessment updatedIcrfAssessment = icrfAssessmentRepository.findById(icrfAssessment.getId()).get();
        // Disconnect from session so that the updates on updatedIcrfAssessment are not directly saved in db
        em.detach(updatedIcrfAssessment);
        updatedIcrfAssessment.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restIcrfAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIcrfAssessment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIcrfAssessment))
            )
            .andExpect(status().isOk());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
        IcrfAssessment testIcrfAssessment = icrfAssessmentList.get(icrfAssessmentList.size() - 1);
        assertThat(testIcrfAssessment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIcrfAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIcrfAssessment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingIcrfAssessment() throws Exception {
        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();
        icrfAssessment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcrfAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, icrfAssessment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIcrfAssessment() throws Exception {
        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();
        icrfAssessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIcrfAssessment() throws Exception {
        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();
        icrfAssessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfAssessmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrfAssessment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIcrfAssessmentWithPatch() throws Exception {
        // Initialize the database
        icrfAssessmentRepository.saveAndFlush(icrfAssessment);

        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();

        // Update the icrfAssessment using partial update
        IcrfAssessment partialUpdatedIcrfAssessment = new IcrfAssessment();
        partialUpdatedIcrfAssessment.setId(icrfAssessment.getId());

        partialUpdatedIcrfAssessment.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restIcrfAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcrfAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcrfAssessment))
            )
            .andExpect(status().isOk());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
        IcrfAssessment testIcrfAssessment = icrfAssessmentList.get(icrfAssessmentList.size() - 1);
        assertThat(testIcrfAssessment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIcrfAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIcrfAssessment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateIcrfAssessmentWithPatch() throws Exception {
        // Initialize the database
        icrfAssessmentRepository.saveAndFlush(icrfAssessment);

        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();

        // Update the icrfAssessment using partial update
        IcrfAssessment partialUpdatedIcrfAssessment = new IcrfAssessment();
        partialUpdatedIcrfAssessment.setId(icrfAssessment.getId());

        partialUpdatedIcrfAssessment.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restIcrfAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcrfAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcrfAssessment))
            )
            .andExpect(status().isOk());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
        IcrfAssessment testIcrfAssessment = icrfAssessmentList.get(icrfAssessmentList.size() - 1);
        assertThat(testIcrfAssessment.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIcrfAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIcrfAssessment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingIcrfAssessment() throws Exception {
        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();
        icrfAssessment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcrfAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, icrfAssessment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIcrfAssessment() throws Exception {
        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();
        icrfAssessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIcrfAssessment() throws Exception {
        int databaseSizeBeforeUpdate = icrfAssessmentRepository.findAll().size();
        icrfAssessment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(icrfAssessment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IcrfAssessment in the database
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIcrfAssessment() throws Exception {
        // Initialize the database
        icrfAssessmentRepository.saveAndFlush(icrfAssessment);

        int databaseSizeBeforeDelete = icrfAssessmentRepository.findAll().size();

        // Delete the icrfAssessment
        restIcrfAssessmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, icrfAssessment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IcrfAssessment> icrfAssessmentList = icrfAssessmentRepository.findAll();
        assertThat(icrfAssessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
