package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PersonalDataType;
import com.mycompany.myapp.repository.PersonalDataTypeRepository;
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
 * Integration tests for the {@link PersonalDataTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalDataTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personal-data-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalDataTypeRepository personalDataTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalDataTypeMockMvc;

    private PersonalDataType personalDataType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDataType createEntity(EntityManager em) {
        PersonalDataType personalDataType = new PersonalDataType().name(DEFAULT_NAME);
        return personalDataType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalDataType createUpdatedEntity(EntityManager em) {
        PersonalDataType personalDataType = new PersonalDataType().name(UPDATED_NAME);
        return personalDataType;
    }

    @BeforeEach
    public void initTest() {
        personalDataType = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalDataType() throws Exception {
        int databaseSizeBeforeCreate = personalDataTypeRepository.findAll().size();
        // Create the PersonalDataType
        restPersonalDataTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isCreated());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalDataType testPersonalDataType = personalDataTypeList.get(personalDataTypeList.size() - 1);
        assertThat(testPersonalDataType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPersonalDataTypeWithExistingId() throws Exception {
        // Create the PersonalDataType with an existing ID
        personalDataType.setId(1L);

        int databaseSizeBeforeCreate = personalDataTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalDataTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalDataTypes() throws Exception {
        // Initialize the database
        personalDataTypeRepository.saveAndFlush(personalDataType);

        // Get all the personalDataTypeList
        restPersonalDataTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalDataType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPersonalDataType() throws Exception {
        // Initialize the database
        personalDataTypeRepository.saveAndFlush(personalDataType);

        // Get the personalDataType
        restPersonalDataTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, personalDataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalDataType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPersonalDataType() throws Exception {
        // Get the personalDataType
        restPersonalDataTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonalDataType() throws Exception {
        // Initialize the database
        personalDataTypeRepository.saveAndFlush(personalDataType);

        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();

        // Update the personalDataType
        PersonalDataType updatedPersonalDataType = personalDataTypeRepository.findById(personalDataType.getId()).get();
        // Disconnect from session so that the updates on updatedPersonalDataType are not directly saved in db
        em.detach(updatedPersonalDataType);
        updatedPersonalDataType.name(UPDATED_NAME);

        restPersonalDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonalDataType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonalDataType))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
        PersonalDataType testPersonalDataType = personalDataTypeList.get(personalDataTypeList.size() - 1);
        assertThat(testPersonalDataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPersonalDataType() throws Exception {
        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();
        personalDataType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalDataType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalDataType() throws Exception {
        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();
        personalDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalDataType() throws Exception {
        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();
        personalDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalDataTypeWithPatch() throws Exception {
        // Initialize the database
        personalDataTypeRepository.saveAndFlush(personalDataType);

        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();

        // Update the personalDataType using partial update
        PersonalDataType partialUpdatedPersonalDataType = new PersonalDataType();
        partialUpdatedPersonalDataType.setId(personalDataType.getId());

        restPersonalDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDataType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalDataType))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
        PersonalDataType testPersonalDataType = personalDataTypeList.get(personalDataTypeList.size() - 1);
        assertThat(testPersonalDataType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePersonalDataTypeWithPatch() throws Exception {
        // Initialize the database
        personalDataTypeRepository.saveAndFlush(personalDataType);

        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();

        // Update the personalDataType using partial update
        PersonalDataType partialUpdatedPersonalDataType = new PersonalDataType();
        partialUpdatedPersonalDataType.setId(personalDataType.getId());

        partialUpdatedPersonalDataType.name(UPDATED_NAME);

        restPersonalDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalDataType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalDataType))
            )
            .andExpect(status().isOk());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
        PersonalDataType testPersonalDataType = personalDataTypeList.get(personalDataTypeList.size() - 1);
        assertThat(testPersonalDataType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalDataType() throws Exception {
        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();
        personalDataType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalDataType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalDataType() throws Exception {
        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();
        personalDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalDataType() throws Exception {
        int databaseSizeBeforeUpdate = personalDataTypeRepository.findAll().size();
        personalDataType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalDataTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalDataType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalDataType in the database
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalDataType() throws Exception {
        // Initialize the database
        personalDataTypeRepository.saveAndFlush(personalDataType);

        int databaseSizeBeforeDelete = personalDataTypeRepository.findAll().size();

        // Delete the personalDataType
        restPersonalDataTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalDataType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalDataType> personalDataTypeList = personalDataTypeRepository.findAll();
        assertThat(personalDataTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
