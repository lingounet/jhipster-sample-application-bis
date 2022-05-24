package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Identity;
import com.mycompany.myapp.domain.enumeration.Process;
import com.mycompany.myapp.repository.IdentityRepository;
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
 * Integration tests for the {@link IdentityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdentityResourceIT {

    private static final String DEFAULT_APPLICATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SO = "AAAAAAAAAA";
    private static final String UPDATED_SO = "BBBBBBBBBB";

    private static final Process DEFAULT_PROCESS = Process.LIGHT;
    private static final Process UPDATED_PROCESS = Process.CLASSIC;

    private static final String ENTITY_API_URL = "/api/identities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IdentityRepository identityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdentityMockMvc;

    private Identity identity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Identity createEntity(EntityManager em) {
        Identity identity = new Identity().applicationName(DEFAULT_APPLICATION_NAME).so(DEFAULT_SO).process(DEFAULT_PROCESS);
        return identity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Identity createUpdatedEntity(EntityManager em) {
        Identity identity = new Identity().applicationName(UPDATED_APPLICATION_NAME).so(UPDATED_SO).process(UPDATED_PROCESS);
        return identity;
    }

    @BeforeEach
    public void initTest() {
        identity = createEntity(em);
    }

    @Test
    @Transactional
    void createIdentity() throws Exception {
        int databaseSizeBeforeCreate = identityRepository.findAll().size();
        // Create the Identity
        restIdentityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(identity)))
            .andExpect(status().isCreated());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeCreate + 1);
        Identity testIdentity = identityList.get(identityList.size() - 1);
        assertThat(testIdentity.getApplicationName()).isEqualTo(DEFAULT_APPLICATION_NAME);
        assertThat(testIdentity.getSo()).isEqualTo(DEFAULT_SO);
        assertThat(testIdentity.getProcess()).isEqualTo(DEFAULT_PROCESS);
    }

    @Test
    @Transactional
    void createIdentityWithExistingId() throws Exception {
        // Create the Identity with an existing ID
        identity.setId(1L);

        int databaseSizeBeforeCreate = identityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdentityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(identity)))
            .andExpect(status().isBadRequest());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApplicationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = identityRepository.findAll().size();
        // set the field null
        identity.setApplicationName(null);

        // Create the Identity, which fails.

        restIdentityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(identity)))
            .andExpect(status().isBadRequest());

        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSoIsRequired() throws Exception {
        int databaseSizeBeforeTest = identityRepository.findAll().size();
        // set the field null
        identity.setSo(null);

        // Create the Identity, which fails.

        restIdentityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(identity)))
            .andExpect(status().isBadRequest());

        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdentities() throws Exception {
        // Initialize the database
        identityRepository.saveAndFlush(identity);

        // Get all the identityList
        restIdentityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identity.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationName").value(hasItem(DEFAULT_APPLICATION_NAME)))
            .andExpect(jsonPath("$.[*].so").value(hasItem(DEFAULT_SO)))
            .andExpect(jsonPath("$.[*].process").value(hasItem(DEFAULT_PROCESS.toString())));
    }

    @Test
    @Transactional
    void getIdentity() throws Exception {
        // Initialize the database
        identityRepository.saveAndFlush(identity);

        // Get the identity
        restIdentityMockMvc
            .perform(get(ENTITY_API_URL_ID, identity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(identity.getId().intValue()))
            .andExpect(jsonPath("$.applicationName").value(DEFAULT_APPLICATION_NAME))
            .andExpect(jsonPath("$.so").value(DEFAULT_SO))
            .andExpect(jsonPath("$.process").value(DEFAULT_PROCESS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIdentity() throws Exception {
        // Get the identity
        restIdentityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIdentity() throws Exception {
        // Initialize the database
        identityRepository.saveAndFlush(identity);

        int databaseSizeBeforeUpdate = identityRepository.findAll().size();

        // Update the identity
        Identity updatedIdentity = identityRepository.findById(identity.getId()).get();
        // Disconnect from session so that the updates on updatedIdentity are not directly saved in db
        em.detach(updatedIdentity);
        updatedIdentity.applicationName(UPDATED_APPLICATION_NAME).so(UPDATED_SO).process(UPDATED_PROCESS);

        restIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIdentity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIdentity))
            )
            .andExpect(status().isOk());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
        Identity testIdentity = identityList.get(identityList.size() - 1);
        assertThat(testIdentity.getApplicationName()).isEqualTo(UPDATED_APPLICATION_NAME);
        assertThat(testIdentity.getSo()).isEqualTo(UPDATED_SO);
        assertThat(testIdentity.getProcess()).isEqualTo(UPDATED_PROCESS);
    }

    @Test
    @Transactional
    void putNonExistingIdentity() throws Exception {
        int databaseSizeBeforeUpdate = identityRepository.findAll().size();
        identity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(identity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdentity() throws Exception {
        int databaseSizeBeforeUpdate = identityRepository.findAll().size();
        identity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(identity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdentity() throws Exception {
        int databaseSizeBeforeUpdate = identityRepository.findAll().size();
        identity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(identity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdentityWithPatch() throws Exception {
        // Initialize the database
        identityRepository.saveAndFlush(identity);

        int databaseSizeBeforeUpdate = identityRepository.findAll().size();

        // Update the identity using partial update
        Identity partialUpdatedIdentity = new Identity();
        partialUpdatedIdentity.setId(identity.getId());

        restIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentity))
            )
            .andExpect(status().isOk());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
        Identity testIdentity = identityList.get(identityList.size() - 1);
        assertThat(testIdentity.getApplicationName()).isEqualTo(DEFAULT_APPLICATION_NAME);
        assertThat(testIdentity.getSo()).isEqualTo(DEFAULT_SO);
        assertThat(testIdentity.getProcess()).isEqualTo(DEFAULT_PROCESS);
    }

    @Test
    @Transactional
    void fullUpdateIdentityWithPatch() throws Exception {
        // Initialize the database
        identityRepository.saveAndFlush(identity);

        int databaseSizeBeforeUpdate = identityRepository.findAll().size();

        // Update the identity using partial update
        Identity partialUpdatedIdentity = new Identity();
        partialUpdatedIdentity.setId(identity.getId());

        partialUpdatedIdentity.applicationName(UPDATED_APPLICATION_NAME).so(UPDATED_SO).process(UPDATED_PROCESS);

        restIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentity))
            )
            .andExpect(status().isOk());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
        Identity testIdentity = identityList.get(identityList.size() - 1);
        assertThat(testIdentity.getApplicationName()).isEqualTo(UPDATED_APPLICATION_NAME);
        assertThat(testIdentity.getSo()).isEqualTo(UPDATED_SO);
        assertThat(testIdentity.getProcess()).isEqualTo(UPDATED_PROCESS);
    }

    @Test
    @Transactional
    void patchNonExistingIdentity() throws Exception {
        int databaseSizeBeforeUpdate = identityRepository.findAll().size();
        identity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, identity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(identity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdentity() throws Exception {
        int databaseSizeBeforeUpdate = identityRepository.findAll().size();
        identity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(identity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdentity() throws Exception {
        int databaseSizeBeforeUpdate = identityRepository.findAll().size();
        identity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(identity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Identity in the database
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdentity() throws Exception {
        // Initialize the database
        identityRepository.saveAndFlush(identity);

        int databaseSizeBeforeDelete = identityRepository.findAll().size();

        // Delete the identity
        restIdentityMockMvc
            .perform(delete(ENTITY_API_URL_ID, identity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Identity> identityList = identityRepository.findAll();
        assertThat(identityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
