package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Psat;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.PsatRepository;
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
 * Integration tests for the {@link PsatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PsatResourceIT {

    private static final String DEFAULT_AML_ID = "AAAAAAAAAA";
    private static final String UPDATED_AML_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.DRAFT;
    private static final Status UPDATED_STATUS = Status.SECURITY_INTERVIEW;

    private static final String ENTITY_API_URL = "/api/psats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PsatRepository psatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPsatMockMvc;

    private Psat psat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Psat createEntity(EntityManager em) {
        Psat psat = new Psat().amlId(DEFAULT_AML_ID).owner(DEFAULT_OWNER).status(DEFAULT_STATUS);
        return psat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Psat createUpdatedEntity(EntityManager em) {
        Psat psat = new Psat().amlId(UPDATED_AML_ID).owner(UPDATED_OWNER).status(UPDATED_STATUS);
        return psat;
    }

    @BeforeEach
    public void initTest() {
        psat = createEntity(em);
    }

    @Test
    @Transactional
    void createPsat() throws Exception {
        int databaseSizeBeforeCreate = psatRepository.findAll().size();
        // Create the Psat
        restPsatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isCreated());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeCreate + 1);
        Psat testPsat = psatList.get(psatList.size() - 1);
        assertThat(testPsat.getAmlId()).isEqualTo(DEFAULT_AML_ID);
        assertThat(testPsat.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testPsat.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPsatWithExistingId() throws Exception {
        // Create the Psat with an existing ID
        psat.setId(1L);

        int databaseSizeBeforeCreate = psatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPsatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isBadRequest());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmlIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = psatRepository.findAll().size();
        // set the field null
        psat.setAmlId(null);

        // Create the Psat, which fails.

        restPsatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isBadRequest());

        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = psatRepository.findAll().size();
        // set the field null
        psat.setOwner(null);

        // Create the Psat, which fails.

        restPsatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isBadRequest());

        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = psatRepository.findAll().size();
        // set the field null
        psat.setStatus(null);

        // Create the Psat, which fails.

        restPsatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isBadRequest());

        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPsats() throws Exception {
        // Initialize the database
        psatRepository.saveAndFlush(psat);

        // Get all the psatList
        restPsatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(psat.getId().intValue())))
            .andExpect(jsonPath("$.[*].amlId").value(hasItem(DEFAULT_AML_ID)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPsat() throws Exception {
        // Initialize the database
        psatRepository.saveAndFlush(psat);

        // Get the psat
        restPsatMockMvc
            .perform(get(ENTITY_API_URL_ID, psat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(psat.getId().intValue()))
            .andExpect(jsonPath("$.amlId").value(DEFAULT_AML_ID))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPsat() throws Exception {
        // Get the psat
        restPsatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPsat() throws Exception {
        // Initialize the database
        psatRepository.saveAndFlush(psat);

        int databaseSizeBeforeUpdate = psatRepository.findAll().size();

        // Update the psat
        Psat updatedPsat = psatRepository.findById(psat.getId()).get();
        // Disconnect from session so that the updates on updatedPsat are not directly saved in db
        em.detach(updatedPsat);
        updatedPsat.amlId(UPDATED_AML_ID).owner(UPDATED_OWNER).status(UPDATED_STATUS);

        restPsatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPsat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPsat))
            )
            .andExpect(status().isOk());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
        Psat testPsat = psatList.get(psatList.size() - 1);
        assertThat(testPsat.getAmlId()).isEqualTo(UPDATED_AML_ID);
        assertThat(testPsat.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPsat.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPsat() throws Exception {
        int databaseSizeBeforeUpdate = psatRepository.findAll().size();
        psat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPsatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, psat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(psat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPsat() throws Exception {
        int databaseSizeBeforeUpdate = psatRepository.findAll().size();
        psat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPsatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(psat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPsat() throws Exception {
        int databaseSizeBeforeUpdate = psatRepository.findAll().size();
        psat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPsatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePsatWithPatch() throws Exception {
        // Initialize the database
        psatRepository.saveAndFlush(psat);

        int databaseSizeBeforeUpdate = psatRepository.findAll().size();

        // Update the psat using partial update
        Psat partialUpdatedPsat = new Psat();
        partialUpdatedPsat.setId(psat.getId());

        partialUpdatedPsat.owner(UPDATED_OWNER).status(UPDATED_STATUS);

        restPsatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPsat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPsat))
            )
            .andExpect(status().isOk());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
        Psat testPsat = psatList.get(psatList.size() - 1);
        assertThat(testPsat.getAmlId()).isEqualTo(DEFAULT_AML_ID);
        assertThat(testPsat.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPsat.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePsatWithPatch() throws Exception {
        // Initialize the database
        psatRepository.saveAndFlush(psat);

        int databaseSizeBeforeUpdate = psatRepository.findAll().size();

        // Update the psat using partial update
        Psat partialUpdatedPsat = new Psat();
        partialUpdatedPsat.setId(psat.getId());

        partialUpdatedPsat.amlId(UPDATED_AML_ID).owner(UPDATED_OWNER).status(UPDATED_STATUS);

        restPsatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPsat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPsat))
            )
            .andExpect(status().isOk());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
        Psat testPsat = psatList.get(psatList.size() - 1);
        assertThat(testPsat.getAmlId()).isEqualTo(UPDATED_AML_ID);
        assertThat(testPsat.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPsat.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPsat() throws Exception {
        int databaseSizeBeforeUpdate = psatRepository.findAll().size();
        psat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPsatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, psat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(psat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPsat() throws Exception {
        int databaseSizeBeforeUpdate = psatRepository.findAll().size();
        psat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPsatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(psat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPsat() throws Exception {
        int databaseSizeBeforeUpdate = psatRepository.findAll().size();
        psat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPsatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(psat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Psat in the database
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePsat() throws Exception {
        // Initialize the database
        psatRepository.saveAndFlush(psat);

        int databaseSizeBeforeDelete = psatRepository.findAll().size();

        // Delete the psat
        restPsatMockMvc
            .perform(delete(ENTITY_API_URL_ID, psat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Psat> psatList = psatRepository.findAll();
        assertThat(psatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
