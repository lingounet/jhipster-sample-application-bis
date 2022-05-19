package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ApplicationType;
import com.mycompany.myapp.repository.ApplicationTypeRepository;
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
 * Integration tests for the {@link ApplicationTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationTypeRepository applicationTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationTypeMockMvc;

    private ApplicationType applicationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationType createEntity(EntityManager em) {
        ApplicationType applicationType = new ApplicationType().name(DEFAULT_NAME);
        return applicationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationType createUpdatedEntity(EntityManager em) {
        ApplicationType applicationType = new ApplicationType().name(UPDATED_NAME);
        return applicationType;
    }

    @BeforeEach
    public void initTest() {
        applicationType = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationType() throws Exception {
        int databaseSizeBeforeCreate = applicationTypeRepository.findAll().size();
        // Create the ApplicationType
        restApplicationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationType testApplicationType = applicationTypeList.get(applicationTypeList.size() - 1);
        assertThat(testApplicationType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createApplicationTypeWithExistingId() throws Exception {
        // Create the ApplicationType with an existing ID
        applicationType.setId(1L);

        int databaseSizeBeforeCreate = applicationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplicationTypes() throws Exception {
        // Initialize the database
        applicationTypeRepository.saveAndFlush(applicationType);

        // Get all the applicationTypeList
        restApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getApplicationType() throws Exception {
        // Initialize the database
        applicationTypeRepository.saveAndFlush(applicationType);

        // Get the applicationType
        restApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingApplicationType() throws Exception {
        // Get the applicationType
        restApplicationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicationType() throws Exception {
        // Initialize the database
        applicationTypeRepository.saveAndFlush(applicationType);

        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();

        // Update the applicationType
        ApplicationType updatedApplicationType = applicationTypeRepository.findById(applicationType.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationType are not directly saved in db
        em.detach(updatedApplicationType);
        updatedApplicationType.name(UPDATED_NAME);

        restApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationType))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
        ApplicationType testApplicationType = applicationTypeList.get(applicationTypeList.size() - 1);
        assertThat(testApplicationType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();
        applicationType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();
        applicationType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();
        applicationType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationTypeWithPatch() throws Exception {
        // Initialize the database
        applicationTypeRepository.saveAndFlush(applicationType);

        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();

        // Update the applicationType using partial update
        ApplicationType partialUpdatedApplicationType = new ApplicationType();
        partialUpdatedApplicationType.setId(applicationType.getId());

        partialUpdatedApplicationType.name(UPDATED_NAME);

        restApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationType))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
        ApplicationType testApplicationType = applicationTypeList.get(applicationTypeList.size() - 1);
        assertThat(testApplicationType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateApplicationTypeWithPatch() throws Exception {
        // Initialize the database
        applicationTypeRepository.saveAndFlush(applicationType);

        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();

        // Update the applicationType using partial update
        ApplicationType partialUpdatedApplicationType = new ApplicationType();
        partialUpdatedApplicationType.setId(applicationType.getId());

        partialUpdatedApplicationType.name(UPDATED_NAME);

        restApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationType))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
        ApplicationType testApplicationType = applicationTypeList.get(applicationTypeList.size() - 1);
        assertThat(testApplicationType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();
        applicationType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();
        applicationType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = applicationTypeRepository.findAll().size();
        applicationType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationType in the database
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationType() throws Exception {
        // Initialize the database
        applicationTypeRepository.saveAndFlush(applicationType);

        int databaseSizeBeforeDelete = applicationTypeRepository.findAll().size();

        // Delete the applicationType
        restApplicationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationType> applicationTypeList = applicationTypeRepository.findAll();
        assertThat(applicationTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
