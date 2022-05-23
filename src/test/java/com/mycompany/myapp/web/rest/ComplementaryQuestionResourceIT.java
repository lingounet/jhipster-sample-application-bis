package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ComplementaryQuestion;
import com.mycompany.myapp.repository.ComplementaryQuestionRepository;
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
 * Integration tests for the {@link ComplementaryQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComplementaryQuestionResourceIT {

    private static final Boolean DEFAULT_INTERNET = false;
    private static final Boolean UPDATED_INTERNET = true;

    private static final Boolean DEFAULT_DEVELOPMENT = false;
    private static final Boolean UPDATED_DEVELOPMENT = true;

    private static final Boolean DEFAULT_CONFIGURATION = false;
    private static final Boolean UPDATED_CONFIGURATION = true;

    private static final Boolean DEFAULT_CLOUD = false;
    private static final Boolean UPDATED_CLOUD = true;

    private static final Boolean DEFAULT_INTERNAL = false;
    private static final Boolean UPDATED_INTERNAL = true;

    private static final Boolean DEFAULT_PARTNER = false;
    private static final Boolean UPDATED_PARTNER = true;

    private static final Integer DEFAULT_USERS = 1;
    private static final Integer UPDATED_USERS = 2;

    private static final String ENTITY_API_URL = "/api/complementary-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComplementaryQuestionRepository complementaryQuestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComplementaryQuestionMockMvc;

    private ComplementaryQuestion complementaryQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComplementaryQuestion createEntity(EntityManager em) {
        ComplementaryQuestion complementaryQuestion = new ComplementaryQuestion()
            .internet(DEFAULT_INTERNET)
            .development(DEFAULT_DEVELOPMENT)
            .configuration(DEFAULT_CONFIGURATION)
            .cloud(DEFAULT_CLOUD)
            .internal(DEFAULT_INTERNAL)
            .partner(DEFAULT_PARTNER)
            .users(DEFAULT_USERS);
        return complementaryQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComplementaryQuestion createUpdatedEntity(EntityManager em) {
        ComplementaryQuestion complementaryQuestion = new ComplementaryQuestion()
            .internet(UPDATED_INTERNET)
            .development(UPDATED_DEVELOPMENT)
            .configuration(UPDATED_CONFIGURATION)
            .cloud(UPDATED_CLOUD)
            .internal(UPDATED_INTERNAL)
            .partner(UPDATED_PARTNER)
            .users(UPDATED_USERS);
        return complementaryQuestion;
    }

    @BeforeEach
    public void initTest() {
        complementaryQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createComplementaryQuestion() throws Exception {
        int databaseSizeBeforeCreate = complementaryQuestionRepository.findAll().size();
        // Create the ComplementaryQuestion
        restComplementaryQuestionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isCreated());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        ComplementaryQuestion testComplementaryQuestion = complementaryQuestionList.get(complementaryQuestionList.size() - 1);
        assertThat(testComplementaryQuestion.getInternet()).isEqualTo(DEFAULT_INTERNET);
        assertThat(testComplementaryQuestion.getDevelopment()).isEqualTo(DEFAULT_DEVELOPMENT);
        assertThat(testComplementaryQuestion.getConfiguration()).isEqualTo(DEFAULT_CONFIGURATION);
        assertThat(testComplementaryQuestion.getCloud()).isEqualTo(DEFAULT_CLOUD);
        assertThat(testComplementaryQuestion.getInternal()).isEqualTo(DEFAULT_INTERNAL);
        assertThat(testComplementaryQuestion.getPartner()).isEqualTo(DEFAULT_PARTNER);
        assertThat(testComplementaryQuestion.getUsers()).isEqualTo(DEFAULT_USERS);
    }

    @Test
    @Transactional
    void createComplementaryQuestionWithExistingId() throws Exception {
        // Create the ComplementaryQuestion with an existing ID
        complementaryQuestion.setId(1L);

        int databaseSizeBeforeCreate = complementaryQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplementaryQuestionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComplementaryQuestions() throws Exception {
        // Initialize the database
        complementaryQuestionRepository.saveAndFlush(complementaryQuestion);

        // Get all the complementaryQuestionList
        restComplementaryQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complementaryQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].internet").value(hasItem(DEFAULT_INTERNET.booleanValue())))
            .andExpect(jsonPath("$.[*].development").value(hasItem(DEFAULT_DEVELOPMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION.booleanValue())))
            .andExpect(jsonPath("$.[*].cloud").value(hasItem(DEFAULT_CLOUD.booleanValue())))
            .andExpect(jsonPath("$.[*].internal").value(hasItem(DEFAULT_INTERNAL.booleanValue())))
            .andExpect(jsonPath("$.[*].partner").value(hasItem(DEFAULT_PARTNER.booleanValue())))
            .andExpect(jsonPath("$.[*].users").value(hasItem(DEFAULT_USERS)));
    }

    @Test
    @Transactional
    void getComplementaryQuestion() throws Exception {
        // Initialize the database
        complementaryQuestionRepository.saveAndFlush(complementaryQuestion);

        // Get the complementaryQuestion
        restComplementaryQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, complementaryQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(complementaryQuestion.getId().intValue()))
            .andExpect(jsonPath("$.internet").value(DEFAULT_INTERNET.booleanValue()))
            .andExpect(jsonPath("$.development").value(DEFAULT_DEVELOPMENT.booleanValue()))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION.booleanValue()))
            .andExpect(jsonPath("$.cloud").value(DEFAULT_CLOUD.booleanValue()))
            .andExpect(jsonPath("$.internal").value(DEFAULT_INTERNAL.booleanValue()))
            .andExpect(jsonPath("$.partner").value(DEFAULT_PARTNER.booleanValue()))
            .andExpect(jsonPath("$.users").value(DEFAULT_USERS));
    }

    @Test
    @Transactional
    void getNonExistingComplementaryQuestion() throws Exception {
        // Get the complementaryQuestion
        restComplementaryQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComplementaryQuestion() throws Exception {
        // Initialize the database
        complementaryQuestionRepository.saveAndFlush(complementaryQuestion);

        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();

        // Update the complementaryQuestion
        ComplementaryQuestion updatedComplementaryQuestion = complementaryQuestionRepository.findById(complementaryQuestion.getId()).get();
        // Disconnect from session so that the updates on updatedComplementaryQuestion are not directly saved in db
        em.detach(updatedComplementaryQuestion);
        updatedComplementaryQuestion
            .internet(UPDATED_INTERNET)
            .development(UPDATED_DEVELOPMENT)
            .configuration(UPDATED_CONFIGURATION)
            .cloud(UPDATED_CLOUD)
            .internal(UPDATED_INTERNAL)
            .partner(UPDATED_PARTNER)
            .users(UPDATED_USERS);

        restComplementaryQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComplementaryQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComplementaryQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
        ComplementaryQuestion testComplementaryQuestion = complementaryQuestionList.get(complementaryQuestionList.size() - 1);
        assertThat(testComplementaryQuestion.getInternet()).isEqualTo(UPDATED_INTERNET);
        assertThat(testComplementaryQuestion.getDevelopment()).isEqualTo(UPDATED_DEVELOPMENT);
        assertThat(testComplementaryQuestion.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testComplementaryQuestion.getCloud()).isEqualTo(UPDATED_CLOUD);
        assertThat(testComplementaryQuestion.getInternal()).isEqualTo(UPDATED_INTERNAL);
        assertThat(testComplementaryQuestion.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testComplementaryQuestion.getUsers()).isEqualTo(UPDATED_USERS);
    }

    @Test
    @Transactional
    void putNonExistingComplementaryQuestion() throws Exception {
        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();
        complementaryQuestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplementaryQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, complementaryQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComplementaryQuestion() throws Exception {
        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();
        complementaryQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplementaryQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComplementaryQuestion() throws Exception {
        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();
        complementaryQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplementaryQuestionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComplementaryQuestionWithPatch() throws Exception {
        // Initialize the database
        complementaryQuestionRepository.saveAndFlush(complementaryQuestion);

        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();

        // Update the complementaryQuestion using partial update
        ComplementaryQuestion partialUpdatedComplementaryQuestion = new ComplementaryQuestion();
        partialUpdatedComplementaryQuestion.setId(complementaryQuestion.getId());

        partialUpdatedComplementaryQuestion.development(UPDATED_DEVELOPMENT).configuration(UPDATED_CONFIGURATION).users(UPDATED_USERS);

        restComplementaryQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplementaryQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplementaryQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
        ComplementaryQuestion testComplementaryQuestion = complementaryQuestionList.get(complementaryQuestionList.size() - 1);
        assertThat(testComplementaryQuestion.getInternet()).isEqualTo(DEFAULT_INTERNET);
        assertThat(testComplementaryQuestion.getDevelopment()).isEqualTo(UPDATED_DEVELOPMENT);
        assertThat(testComplementaryQuestion.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testComplementaryQuestion.getCloud()).isEqualTo(DEFAULT_CLOUD);
        assertThat(testComplementaryQuestion.getInternal()).isEqualTo(DEFAULT_INTERNAL);
        assertThat(testComplementaryQuestion.getPartner()).isEqualTo(DEFAULT_PARTNER);
        assertThat(testComplementaryQuestion.getUsers()).isEqualTo(UPDATED_USERS);
    }

    @Test
    @Transactional
    void fullUpdateComplementaryQuestionWithPatch() throws Exception {
        // Initialize the database
        complementaryQuestionRepository.saveAndFlush(complementaryQuestion);

        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();

        // Update the complementaryQuestion using partial update
        ComplementaryQuestion partialUpdatedComplementaryQuestion = new ComplementaryQuestion();
        partialUpdatedComplementaryQuestion.setId(complementaryQuestion.getId());

        partialUpdatedComplementaryQuestion
            .internet(UPDATED_INTERNET)
            .development(UPDATED_DEVELOPMENT)
            .configuration(UPDATED_CONFIGURATION)
            .cloud(UPDATED_CLOUD)
            .internal(UPDATED_INTERNAL)
            .partner(UPDATED_PARTNER)
            .users(UPDATED_USERS);

        restComplementaryQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComplementaryQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComplementaryQuestion))
            )
            .andExpect(status().isOk());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
        ComplementaryQuestion testComplementaryQuestion = complementaryQuestionList.get(complementaryQuestionList.size() - 1);
        assertThat(testComplementaryQuestion.getInternet()).isEqualTo(UPDATED_INTERNET);
        assertThat(testComplementaryQuestion.getDevelopment()).isEqualTo(UPDATED_DEVELOPMENT);
        assertThat(testComplementaryQuestion.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testComplementaryQuestion.getCloud()).isEqualTo(UPDATED_CLOUD);
        assertThat(testComplementaryQuestion.getInternal()).isEqualTo(UPDATED_INTERNAL);
        assertThat(testComplementaryQuestion.getPartner()).isEqualTo(UPDATED_PARTNER);
        assertThat(testComplementaryQuestion.getUsers()).isEqualTo(UPDATED_USERS);
    }

    @Test
    @Transactional
    void patchNonExistingComplementaryQuestion() throws Exception {
        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();
        complementaryQuestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComplementaryQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, complementaryQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComplementaryQuestion() throws Exception {
        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();
        complementaryQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplementaryQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComplementaryQuestion() throws Exception {
        int databaseSizeBeforeUpdate = complementaryQuestionRepository.findAll().size();
        complementaryQuestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComplementaryQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(complementaryQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComplementaryQuestion in the database
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComplementaryQuestion() throws Exception {
        // Initialize the database
        complementaryQuestionRepository.saveAndFlush(complementaryQuestion);

        int databaseSizeBeforeDelete = complementaryQuestionRepository.findAll().size();

        // Delete the complementaryQuestion
        restComplementaryQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, complementaryQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComplementaryQuestion> complementaryQuestionList = complementaryQuestionRepository.findAll();
        assertThat(complementaryQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
