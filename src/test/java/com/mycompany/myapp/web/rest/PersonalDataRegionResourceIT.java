package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PersonalDataRegion;
import com.mycompany.myapp.repository.PersonalDataRegionRepository;
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
 * Integration tests for the {@link PersonalDataRegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalDataRegionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personal-data-regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalDataRegionRepository personalDataRegionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalDataRegionMockMvc;

    private PersonalDataRegion personalDataRegion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDataRegion createEntity(EntityManager em) {
        PersonalDataRegion personalDataRegion = new PersonalDataRegion().name(DEFAULT_NAME);
        return personalDataRegion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDataRegion createUpdatedEntity(EntityManager em) {
        PersonalDataRegion personalDataRegion = new PersonalDataRegion().name(UPDATED_NAME);
        return personalDataRegion;
    }

    @BeforeEach
    public void initTest() {
        personalDataRegion = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalDataRegion() throws Exception {
        int databaseSizeBeforeCreate = personalDataRegionRepository.findAll().size();
        // Create the PersonalDataRegion
        restPersonalDataRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isCreated());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalDataRegion testPersonalDataRegion = personalDataRegionList.get(personalDataRegionList.size() - 1);
        assertThat(testPersonalDataRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPersonalDataRegionWithExistingId() throws Exception {
        // Create the PersonalDataRegion with an existing ID
        personalDataRegion.setId(1L);

        int databaseSizeBeforeCreate = personalDataRegionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalDataRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalDataRegions() throws Exception {
        // Initialize the database
        personalDataRegionRepository.saveAndFlush(personalDataRegion);

        // Get all the personalDataRegionList
        restPersonalDataRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalDataRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPersonalDataRegion() throws Exception {
        // Initialize the database
        personalDataRegionRepository.saveAndFlush(personalDataRegion);

        // Get the personalDataRegion
        restPersonalDataRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, personalDataRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalDataRegion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPersonalDataRegion() throws Exception {
        // Get the personalDataRegion
        restPersonalDataRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonalDataRegion() throws Exception {
        // Initialize the database
        personalDataRegionRepository.saveAndFlush(personalDataRegion);

        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();

        // Update the personalDataRegion
        PersonalDataRegion updatedPersonalDataRegion = personalDataRegionRepository.findById(personalDataRegion.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalDataRegion are not directly saved in db
        em.detach(updatedPersonalDataRegion);
        updatedPersonalDataRegion.name(UPDATED_NAME);

        restPersonalDataRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonalDataRegion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonalDataRegion))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
        PersonalDataRegion testPersonalDataRegion = personalDataRegionList.get(personalDataRegionList.size() - 1);
        assertThat(testPersonalDataRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPersonalDataRegion() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();
        personalDataRegion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDataRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalDataRegion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalDataRegion() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();
        personalDataRegion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalDataRegion() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();
        personalDataRegion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataRegionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalDataRegionWithPatch() throws Exception {
        // Initialize the database
        personalDataRegionRepository.saveAndFlush(personalDataRegion);

        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();

        // Update the personalDataRegion using partial update
        PersonalDataRegion partialUpdatedPersonalDataRegion = new PersonalDataRegion();
        partialUpdatedPersonalDataRegion.setId(personalDataRegion.getId());

        partialUpdatedPersonalDataRegion.name(UPDATED_NAME);

        restPersonalDataRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDataRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalDataRegion))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
        PersonalDataRegion testPersonalDataRegion = personalDataRegionList.get(personalDataRegionList.size() - 1);
        assertThat(testPersonalDataRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePersonalDataRegionWithPatch() throws Exception {
        // Initialize the database
        personalDataRegionRepository.saveAndFlush(personalDataRegion);

        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();

        // Update the personalDataRegion using partial update
        PersonalDataRegion partialUpdatedPersonalDataRegion = new PersonalDataRegion();
        partialUpdatedPersonalDataRegion.setId(personalDataRegion.getId());

        partialUpdatedPersonalDataRegion.name(UPDATED_NAME);

        restPersonalDataRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDataRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalDataRegion))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
        PersonalDataRegion testPersonalDataRegion = personalDataRegionList.get(personalDataRegionList.size() - 1);
        assertThat(testPersonalDataRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalDataRegion() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();
        personalDataRegion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDataRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalDataRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalDataRegion() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();
        personalDataRegion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalDataRegion() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRegionRepository.findAll().size();
        personalDataRegion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataRegionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDataRegion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDataRegion in the database
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalDataRegion() throws Exception {
        // Initialize the database
        personalDataRegionRepository.saveAndFlush(personalDataRegion);

        int databaseSizeBeforeDelete = personalDataRegionRepository.findAll().size();

        // Delete the personalDataRegion
        restPersonalDataRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalDataRegion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalDataRegion> personalDataRegionList = personalDataRegionRepository.findAll();
        assertThat(personalDataRegionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
