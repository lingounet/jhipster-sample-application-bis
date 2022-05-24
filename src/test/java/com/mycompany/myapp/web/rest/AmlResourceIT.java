package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Aml;
import com.mycompany.myapp.repository.AmlRepository;
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
 * Integration tests for the {@link AmlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AmlResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/amls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AmlRepository amlRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAmlMockMvc;

    private Aml aml;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aml createEntity(EntityManager em) {
        Aml aml = new Aml().name(DEFAULT_NAME);
        return aml;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aml createUpdatedEntity(EntityManager em) {
        Aml aml = new Aml().name(UPDATED_NAME);
        return aml;
    }

    @BeforeEach
    public void initTest() {
        aml = createEntity(em);
    }

    @Test
    @Transactional
    void createAml() throws Exception {
        int databaseSizeBeforeCreate = amlRepository.findAll().size();
        // Create the Aml
        restAmlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aml)))
            .andExpect(status().isCreated());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeCreate + 1);
        Aml testAml = amlList.get(amlList.size() - 1);
        assertThat(testAml.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createAmlWithExistingId() throws Exception {
        // Create the Aml with an existing ID
        aml.setId(1L);

        int databaseSizeBeforeCreate = amlRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAmlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aml)))
            .andExpect(status().isBadRequest());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAmls() throws Exception {
        // Initialize the database
        amlRepository.saveAndFlush(aml);

        // Get all the amlList
        restAmlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aml.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAml() throws Exception {
        // Initialize the database
        amlRepository.saveAndFlush(aml);

        // Get the aml
        restAmlMockMvc
            .perform(get(ENTITY_API_URL_ID, aml.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aml.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAml() throws Exception {
        // Get the aml
        restAmlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAml() throws Exception {
        // Initialize the database
        amlRepository.saveAndFlush(aml);

        int databaseSizeBeforeUpdate = amlRepository.findAll().size();

        // Update the aml
        Aml updatedAml = amlRepository.findById(aml.getId()).get();
        // Disconnect from session so that the updates on updatedAml are not directly saved in db
        em.detach(updatedAml);
        updatedAml.name(UPDATED_NAME);

        restAmlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAml.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAml))
            )
            .andExpect(status().isOk());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
        Aml testAml = amlList.get(amlList.size() - 1);
        assertThat(testAml.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAml() throws Exception {
        int databaseSizeBeforeUpdate = amlRepository.findAll().size();
        aml.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aml.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aml))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAml() throws Exception {
        int databaseSizeBeforeUpdate = amlRepository.findAll().size();
        aml.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aml))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAml() throws Exception {
        int databaseSizeBeforeUpdate = amlRepository.findAll().size();
        aml.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmlMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aml)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAmlWithPatch() throws Exception {
        // Initialize the database
        amlRepository.saveAndFlush(aml);

        int databaseSizeBeforeUpdate = amlRepository.findAll().size();

        // Update the aml using partial update
        Aml partialUpdatedAml = new Aml();
        partialUpdatedAml.setId(aml.getId());

        restAmlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAml.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAml))
            )
            .andExpect(status().isOk());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
        Aml testAml = amlList.get(amlList.size() - 1);
        assertThat(testAml.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAmlWithPatch() throws Exception {
        // Initialize the database
        amlRepository.saveAndFlush(aml);

        int databaseSizeBeforeUpdate = amlRepository.findAll().size();

        // Update the aml using partial update
        Aml partialUpdatedAml = new Aml();
        partialUpdatedAml.setId(aml.getId());

        partialUpdatedAml.name(UPDATED_NAME);

        restAmlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAml.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAml))
            )
            .andExpect(status().isOk());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
        Aml testAml = amlList.get(amlList.size() - 1);
        assertThat(testAml.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAml() throws Exception {
        int databaseSizeBeforeUpdate = amlRepository.findAll().size();
        aml.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAmlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aml.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aml))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAml() throws Exception {
        int databaseSizeBeforeUpdate = amlRepository.findAll().size();
        aml.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aml))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAml() throws Exception {
        int databaseSizeBeforeUpdate = amlRepository.findAll().size();
        aml.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAmlMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aml)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aml in the database
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAml() throws Exception {
        // Initialize the database
        amlRepository.saveAndFlush(aml);

        int databaseSizeBeforeDelete = amlRepository.findAll().size();

        // Delete the aml
        restAmlMockMvc.perform(delete(ENTITY_API_URL_ID, aml.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aml> amlList = amlRepository.findAll();
        assertThat(amlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
