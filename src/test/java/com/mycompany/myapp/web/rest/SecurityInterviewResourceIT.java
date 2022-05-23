package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SecurityInterview;
import com.mycompany.myapp.domain.enumeration.Process;
import com.mycompany.myapp.repository.SecurityInterviewRepository;
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
 * Integration tests for the {@link SecurityInterviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SecurityInterviewResourceIT {

    private static final String DEFAULT_APPLICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SO = "AAAAAAAAAA";
    private static final String UPDATED_SO = "BBBBBBBBBB";

    private static final Process DEFAULT_PROCESS = Process.LIGHT;
    private static final Process UPDATED_PROCESS = Process.CLASSIC;

    private static final String ENTITY_API_URL = "/api/security-interviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityInterviewRepository securityInterviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityInterviewMockMvc;

    private SecurityInterview securityInterview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityInterview createEntity(EntityManager em) {
        SecurityInterview securityInterview = new SecurityInterview()
            .applicationName(DEFAULT_APPLICATION_NAME)
            .so(DEFAULT_SO)
            .process(DEFAULT_PROCESS);
        return securityInterview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityInterview createUpdatedEntity(EntityManager em) {
        SecurityInterview securityInterview = new SecurityInterview()
            .applicationName(UPDATED_APPLICATION_NAME)
            .so(UPDATED_SO)
            .process(UPDATED_PROCESS);
        return securityInterview;
    }

    @BeforeEach
    public void initTest() {
        securityInterview = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityInterview() throws Exception {
        int databaseSizeBeforeCreate = securityInterviewRepository.findAll().size();
        // Create the SecurityInterview
        restSecurityInterviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityInterview testSecurityInterview = securityInterviewList.get(securityInterviewList.size() - 1);
        assertThat(testSecurityInterview.getApplicationName()).isEqualTo(DEFAULT_APPLICATION_NAME);
        assertThat(testSecurityInterview.getSo()).isEqualTo(DEFAULT_SO);
        assertThat(testSecurityInterview.getProcess()).isEqualTo(DEFAULT_PROCESS);
    }

    @Test
    @Transactional
    void createSecurityInterviewWithExistingId() throws Exception {
        // Create the SecurityInterview with an existing ID
        securityInterview.setId(1L);

        int databaseSizeBeforeCreate = securityInterviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityInterviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSecurityInterviews() throws Exception {
        // Initialize the database
        securityInterviewRepository.saveAndFlush(securityInterview);

        // Get all the securityInterviewList
        restSecurityInterviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityInterview.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationName").value(hasItem(DEFAULT_APPLICATION_NAME)))
            .andExpect(jsonPath("$.[*].so").value(hasItem(DEFAULT_SO)))
            .andExpect(jsonPath("$.[*].process").value(hasItem(DEFAULT_PROCESS.toString())));
    }

    @Test
    @Transactional
    void getSecurityInterview() throws Exception {
        // Initialize the database
        securityInterviewRepository.saveAndFlush(securityInterview);

        // Get the securityInterview
        restSecurityInterviewMockMvc
            .perform(get(ENTITY_API_URL_ID, securityInterview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityInterview.getId().intValue()))
            .andExpect(jsonPath("$.applicationName").value(DEFAULT_APPLICATION_NAME))
            .andExpect(jsonPath("$.so").value(DEFAULT_SO))
            .andExpect(jsonPath("$.process").value(DEFAULT_PROCESS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSecurityInterview() throws Exception {
        // Get the securityInterview
        restSecurityInterviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityInterview() throws Exception {
        // Initialize the database
        securityInterviewRepository.saveAndFlush(securityInterview);

        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();

        // Update the securityInterview
        SecurityInterview updatedSecurityInterview = securityInterviewRepository.findById(securityInterview.getId()).get();
        // Disconnect from session so that the updates on updatedSecurityInterview are not directly saved in db
        em.detach(updatedSecurityInterview);
        updatedSecurityInterview.applicationName(UPDATED_APPLICATION_NAME).so(UPDATED_SO).process(UPDATED_PROCESS);

        restSecurityInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSecurityInterview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSecurityInterview))
            )
            .andExpect(status().isOk());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
        SecurityInterview testSecurityInterview = securityInterviewList.get(securityInterviewList.size() - 1);
        assertThat(testSecurityInterview.getApplicationName()).isEqualTo(UPDATED_APPLICATION_NAME);
        assertThat(testSecurityInterview.getSo()).isEqualTo(UPDATED_SO);
        assertThat(testSecurityInterview.getProcess()).isEqualTo(UPDATED_PROCESS);
    }

    @Test
    @Transactional
    void putNonExistingSecurityInterview() throws Exception {
        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();
        securityInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityInterview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityInterview() throws Exception {
        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();
        securityInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityInterview() throws Exception {
        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();
        securityInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityInterviewMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSecurityInterviewWithPatch() throws Exception {
        // Initialize the database
        securityInterviewRepository.saveAndFlush(securityInterview);

        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();

        // Update the securityInterview using partial update
        SecurityInterview partialUpdatedSecurityInterview = new SecurityInterview();
        partialUpdatedSecurityInterview.setId(securityInterview.getId());

        partialUpdatedSecurityInterview.so(UPDATED_SO).process(UPDATED_PROCESS);

        restSecurityInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityInterview))
            )
            .andExpect(status().isOk());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
        SecurityInterview testSecurityInterview = securityInterviewList.get(securityInterviewList.size() - 1);
        assertThat(testSecurityInterview.getApplicationName()).isEqualTo(DEFAULT_APPLICATION_NAME);
        assertThat(testSecurityInterview.getSo()).isEqualTo(UPDATED_SO);
        assertThat(testSecurityInterview.getProcess()).isEqualTo(UPDATED_PROCESS);
    }

    @Test
    @Transactional
    void fullUpdateSecurityInterviewWithPatch() throws Exception {
        // Initialize the database
        securityInterviewRepository.saveAndFlush(securityInterview);

        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();

        // Update the securityInterview using partial update
        SecurityInterview partialUpdatedSecurityInterview = new SecurityInterview();
        partialUpdatedSecurityInterview.setId(securityInterview.getId());

        partialUpdatedSecurityInterview.applicationName(UPDATED_APPLICATION_NAME).so(UPDATED_SO).process(UPDATED_PROCESS);

        restSecurityInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityInterview))
            )
            .andExpect(status().isOk());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
        SecurityInterview testSecurityInterview = securityInterviewList.get(securityInterviewList.size() - 1);
        assertThat(testSecurityInterview.getApplicationName()).isEqualTo(UPDATED_APPLICATION_NAME);
        assertThat(testSecurityInterview.getSo()).isEqualTo(UPDATED_SO);
        assertThat(testSecurityInterview.getProcess()).isEqualTo(UPDATED_PROCESS);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityInterview() throws Exception {
        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();
        securityInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityInterview() throws Exception {
        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();
        securityInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityInterview() throws Exception {
        int databaseSizeBeforeUpdate = securityInterviewRepository.findAll().size();
        securityInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityInterview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityInterview in the database
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSecurityInterview() throws Exception {
        // Initialize the database
        securityInterviewRepository.saveAndFlush(securityInterview);

        int databaseSizeBeforeDelete = securityInterviewRepository.findAll().size();

        // Delete the securityInterview
        restSecurityInterviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityInterview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityInterview> securityInterviewList = securityInterviewRepository.findAll();
        assertThat(securityInterviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
