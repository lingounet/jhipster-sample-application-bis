package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Icrf;
import com.mycompany.myapp.repository.IcrfRepository;
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
 * Integration tests for the {@link IcrfResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IcrfResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/icrfs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IcrfRepository icrfRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIcrfMockMvc;

    private Icrf icrf;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icrf createEntity(EntityManager em) {
        Icrf icrf = new Icrf().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return icrf;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icrf createUpdatedEntity(EntityManager em) {
        Icrf icrf = new Icrf().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return icrf;
    }

    @BeforeEach
    public void initTest() {
        icrf = createEntity(em);
    }

    @Test
    @Transactional
    void createIcrf() throws Exception {
        int databaseSizeBeforeCreate = icrfRepository.findAll().size();
        // Create the Icrf
        restIcrfMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrf)))
            .andExpect(status().isCreated());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeCreate + 1);
        Icrf testIcrf = icrfList.get(icrfList.size() - 1);
        assertThat(testIcrf.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testIcrf.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createIcrfWithExistingId() throws Exception {
        // Create the Icrf with an existing ID
        icrf.setId(1L);

        int databaseSizeBeforeCreate = icrfRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIcrfMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrf)))
            .andExpect(status().isBadRequest());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIcrfs() throws Exception {
        // Initialize the database
        icrfRepository.saveAndFlush(icrf);

        // Get all the icrfList
        restIcrfMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icrf.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getIcrf() throws Exception {
        // Initialize the database
        icrfRepository.saveAndFlush(icrf);

        // Get the icrf
        restIcrfMockMvc
            .perform(get(ENTITY_API_URL_ID, icrf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(icrf.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingIcrf() throws Exception {
        // Get the icrf
        restIcrfMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIcrf() throws Exception {
        // Initialize the database
        icrfRepository.saveAndFlush(icrf);

        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();

        // Update the icrf
        Icrf updatedIcrf = icrfRepository.findById(icrf.getId()).get();
        // Disconnect from session so that the updates on updatedIcrf are not directly saved in db
        em.detach(updatedIcrf);
        updatedIcrf.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restIcrfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIcrf.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIcrf))
            )
            .andExpect(status().isOk());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
        Icrf testIcrf = icrfList.get(icrfList.size() - 1);
        assertThat(testIcrf.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIcrf.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingIcrf() throws Exception {
        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();
        icrf.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcrfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, icrf.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icrf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIcrf() throws Exception {
        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();
        icrf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(icrf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIcrf() throws Exception {
        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();
        icrf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(icrf)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIcrfWithPatch() throws Exception {
        // Initialize the database
        icrfRepository.saveAndFlush(icrf);

        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();

        // Update the icrf using partial update
        Icrf partialUpdatedIcrf = new Icrf();
        partialUpdatedIcrf.setId(icrf.getId());

        partialUpdatedIcrf.code(UPDATED_CODE);

        restIcrfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcrf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcrf))
            )
            .andExpect(status().isOk());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
        Icrf testIcrf = icrfList.get(icrfList.size() - 1);
        assertThat(testIcrf.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIcrf.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateIcrfWithPatch() throws Exception {
        // Initialize the database
        icrfRepository.saveAndFlush(icrf);

        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();

        // Update the icrf using partial update
        Icrf partialUpdatedIcrf = new Icrf();
        partialUpdatedIcrf.setId(icrf.getId());

        partialUpdatedIcrf.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restIcrfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIcrf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIcrf))
            )
            .andExpect(status().isOk());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
        Icrf testIcrf = icrfList.get(icrfList.size() - 1);
        assertThat(testIcrf.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIcrf.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingIcrf() throws Exception {
        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();
        icrf.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIcrfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, icrf.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icrf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIcrf() throws Exception {
        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();
        icrf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(icrf))
            )
            .andExpect(status().isBadRequest());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIcrf() throws Exception {
        int databaseSizeBeforeUpdate = icrfRepository.findAll().size();
        icrf.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIcrfMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(icrf)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Icrf in the database
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIcrf() throws Exception {
        // Initialize the database
        icrfRepository.saveAndFlush(icrf);

        int databaseSizeBeforeDelete = icrfRepository.findAll().size();

        // Delete the icrf
        restIcrfMockMvc
            .perform(delete(ENTITY_API_URL_ID, icrf.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Icrf> icrfList = icrfRepository.findAll();
        assertThat(icrfList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
