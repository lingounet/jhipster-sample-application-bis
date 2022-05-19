package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PersonalData;
import com.mycompany.myapp.repository.PersonalDataRepository;
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
 * Integration tests for the {@link PersonalDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalDataResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/personal-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalDataRepository personalDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalDataMockMvc;

    private PersonalData personalData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalData createEntity(EntityManager em) {
        PersonalData personalData = new PersonalData().date(DEFAULT_DATE);
        return personalData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalData createUpdatedEntity(EntityManager em) {
        PersonalData personalData = new PersonalData().date(UPDATED_DATE);
        return personalData;
    }

    @BeforeEach
    public void initTest() {
        personalData = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalData() throws Exception {
        int databaseSizeBeforeCreate = personalDataRepository.findAll().size();
        // Create the PersonalData
        restPersonalDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalData)))
            .andExpect(status().isCreated());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalData testPersonalData = personalDataList.get(personalDataList.size() - 1);
        assertThat(testPersonalData.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPersonalDataWithExistingId() throws Exception {
        // Create the PersonalData with an existing ID
        personalData.setId(1L);

        int databaseSizeBeforeCreate = personalDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalData)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        // Get all the personalDataList
        restPersonalDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalData.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        // Get the personalData
        restPersonalDataMockMvc
            .perform(get(ENTITY_API_URL_ID, personalData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalData.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPersonalData() throws Exception {
        // Get the personalData
        restPersonalDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();

        // Update the personalData
        PersonalData updatedPersonalData = personalDataRepository.findById(personalData.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalData are not directly saved in db
        em.detach(updatedPersonalData);
        updatedPersonalData.date(UPDATED_DATE);

        restPersonalDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonalData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonalData))
            )
            .andExpect(status().isOk());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
        PersonalData testPersonalData = personalDataList.get(personalDataList.size() - 1);
        assertThat(testPersonalData.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();
        personalData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalData))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();
        personalData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalData))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();
        personalData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalDataWithPatch() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();

        // Update the personalData using partial update
        PersonalData partialUpdatedPersonalData = new PersonalData();
        partialUpdatedPersonalData.setId(personalData.getId());

        restPersonalDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalData))
            )
            .andExpect(status().isOk());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
        PersonalData testPersonalData = personalDataList.get(personalDataList.size() - 1);
        assertThat(testPersonalData.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePersonalDataWithPatch() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();

        // Update the personalData using partial update
        PersonalData partialUpdatedPersonalData = new PersonalData();
        partialUpdatedPersonalData.setId(personalData.getId());

        partialUpdatedPersonalData.date(UPDATED_DATE);

        restPersonalDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalData))
            )
            .andExpect(status().isOk());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
        PersonalData testPersonalData = personalDataList.get(personalDataList.size() - 1);
        assertThat(testPersonalData.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();
        personalData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalData))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();
        personalData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalData))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalData() throws Exception {
        int databaseSizeBeforeUpdate = personalDataRepository.findAll().size();
        personalData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personalData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalData in the database
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalData() throws Exception {
        // Initialize the database
        personalDataRepository.saveAndFlush(personalData);

        int databaseSizeBeforeDelete = personalDataRepository.findAll().size();

        // Delete the personalData
        restPersonalDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalData> personalDataList = personalDataRepository.findAll();
        assertThat(personalDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
