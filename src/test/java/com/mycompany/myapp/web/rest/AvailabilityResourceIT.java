package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Availability;
import com.mycompany.myapp.domain.enumeration.Criticality;
import com.mycompany.myapp.domain.enumeration.Criticality;
import com.mycompany.myapp.domain.enumeration.Criticality;
import com.mycompany.myapp.domain.enumeration.Criticality;
import com.mycompany.myapp.domain.enumeration.Criticality;
import com.mycompany.myapp.repository.AvailabilityRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AvailabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvailabilityResourceIT {

    private static final Criticality DEFAULT_FINANCIAL = Criticality.LOW;
    private static final Criticality UPDATED_FINANCIAL = Criticality.MODERATE;

    private static final Criticality DEFAULT_LEGAL = Criticality.LOW;
    private static final Criticality UPDATED_LEGAL = Criticality.MODERATE;

    private static final Criticality DEFAULT_IMAGE = Criticality.LOW;
    private static final Criticality UPDATED_IMAGE = Criticality.MODERATE;

    private static final Criticality DEFAULT_STRATEGY = Criticality.LOW;
    private static final Criticality UPDATED_STRATEGY = Criticality.MODERATE;

    private static final Criticality DEFAULT_OPERATIONAL = Criticality.LOW;
    private static final Criticality UPDATED_OPERATIONAL = Criticality.MODERATE;

    private static final byte[] DEFAULT_TRACEABILITY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TRACEABILITY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TRACEABILITY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TRACEABILITY_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_CONFIDENTIALITY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONFIDENTIALITY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONFIDENTIALITY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONFIDENTIALITY_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_INTEGRITY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INTEGRITY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_INTEGRITY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INTEGRITY_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_CRITICAL = false;
    private static final Boolean UPDATED_CRITICAL = true;

    private static final String ENTITY_API_URL = "/api/availabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvailabilityMockMvc;

    private Availability availability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Availability createEntity(EntityManager em) {
        Availability availability = new Availability()
            .financial(DEFAULT_FINANCIAL)
            .legal(DEFAULT_LEGAL)
            .image(DEFAULT_IMAGE)
            .strategy(DEFAULT_STRATEGY)
            .operational(DEFAULT_OPERATIONAL)
            .traceability(DEFAULT_TRACEABILITY)
            .traceabilityContentType(DEFAULT_TRACEABILITY_CONTENT_TYPE)
            .confidentiality(DEFAULT_CONFIDENTIALITY)
            .confidentialityContentType(DEFAULT_CONFIDENTIALITY_CONTENT_TYPE)
            .integrity(DEFAULT_INTEGRITY)
            .integrityContentType(DEFAULT_INTEGRITY_CONTENT_TYPE)
            .critical(DEFAULT_CRITICAL);
        return availability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Availability createUpdatedEntity(EntityManager em) {
        Availability availability = new Availability()
            .financial(UPDATED_FINANCIAL)
            .legal(UPDATED_LEGAL)
            .image(UPDATED_IMAGE)
            .strategy(UPDATED_STRATEGY)
            .operational(UPDATED_OPERATIONAL)
            .traceability(UPDATED_TRACEABILITY)
            .traceabilityContentType(UPDATED_TRACEABILITY_CONTENT_TYPE)
            .confidentiality(UPDATED_CONFIDENTIALITY)
            .confidentialityContentType(UPDATED_CONFIDENTIALITY_CONTENT_TYPE)
            .integrity(UPDATED_INTEGRITY)
            .integrityContentType(UPDATED_INTEGRITY_CONTENT_TYPE)
            .critical(UPDATED_CRITICAL);
        return availability;
    }

    @BeforeEach
    public void initTest() {
        availability = createEntity(em);
    }

    @Test
    @Transactional
    void createAvailability() throws Exception {
        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();
        // Create the Availability
        restAvailabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(availability)))
            .andExpect(status().isCreated());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getFinancial()).isEqualTo(DEFAULT_FINANCIAL);
        assertThat(testAvailability.getLegal()).isEqualTo(DEFAULT_LEGAL);
        assertThat(testAvailability.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAvailability.getStrategy()).isEqualTo(DEFAULT_STRATEGY);
        assertThat(testAvailability.getOperational()).isEqualTo(DEFAULT_OPERATIONAL);
        assertThat(testAvailability.getTraceability()).isEqualTo(DEFAULT_TRACEABILITY);
        assertThat(testAvailability.getTraceabilityContentType()).isEqualTo(DEFAULT_TRACEABILITY_CONTENT_TYPE);
        assertThat(testAvailability.getConfidentiality()).isEqualTo(DEFAULT_CONFIDENTIALITY);
        assertThat(testAvailability.getConfidentialityContentType()).isEqualTo(DEFAULT_CONFIDENTIALITY_CONTENT_TYPE);
        assertThat(testAvailability.getIntegrity()).isEqualTo(DEFAULT_INTEGRITY);
        assertThat(testAvailability.getIntegrityContentType()).isEqualTo(DEFAULT_INTEGRITY_CONTENT_TYPE);
        assertThat(testAvailability.getCritical()).isEqualTo(DEFAULT_CRITICAL);
    }

    @Test
    @Transactional
    void createAvailabilityWithExistingId() throws Exception {
        // Create the Availability with an existing ID
        availability.setId(1L);

        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(availability)))
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAvailabilities() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
            .andExpect(jsonPath("$.[*].financial").value(hasItem(DEFAULT_FINANCIAL.toString())))
            .andExpect(jsonPath("$.[*].legal").value(hasItem(DEFAULT_LEGAL.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].strategy").value(hasItem(DEFAULT_STRATEGY.toString())))
            .andExpect(jsonPath("$.[*].operational").value(hasItem(DEFAULT_OPERATIONAL.toString())))
            .andExpect(jsonPath("$.[*].traceabilityContentType").value(hasItem(DEFAULT_TRACEABILITY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].traceability").value(hasItem(Base64Utils.encodeToString(DEFAULT_TRACEABILITY))))
            .andExpect(jsonPath("$.[*].confidentialityContentType").value(hasItem(DEFAULT_CONFIDENTIALITY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].confidentiality").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONFIDENTIALITY))))
            .andExpect(jsonPath("$.[*].integrityContentType").value(hasItem(DEFAULT_INTEGRITY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].integrity").value(hasItem(Base64Utils.encodeToString(DEFAULT_INTEGRITY))))
            .andExpect(jsonPath("$.[*].critical").value(hasItem(DEFAULT_CRITICAL.booleanValue())));
    }

    @Test
    @Transactional
    void getAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get the availability
        restAvailabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, availability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(availability.getId().intValue()))
            .andExpect(jsonPath("$.financial").value(DEFAULT_FINANCIAL.toString()))
            .andExpect(jsonPath("$.legal").value(DEFAULT_LEGAL.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.strategy").value(DEFAULT_STRATEGY.toString()))
            .andExpect(jsonPath("$.operational").value(DEFAULT_OPERATIONAL.toString()))
            .andExpect(jsonPath("$.traceabilityContentType").value(DEFAULT_TRACEABILITY_CONTENT_TYPE))
            .andExpect(jsonPath("$.traceability").value(Base64Utils.encodeToString(DEFAULT_TRACEABILITY)))
            .andExpect(jsonPath("$.confidentialityContentType").value(DEFAULT_CONFIDENTIALITY_CONTENT_TYPE))
            .andExpect(jsonPath("$.confidentiality").value(Base64Utils.encodeToString(DEFAULT_CONFIDENTIALITY)))
            .andExpect(jsonPath("$.integrityContentType").value(DEFAULT_INTEGRITY_CONTENT_TYPE))
            .andExpect(jsonPath("$.integrity").value(Base64Utils.encodeToString(DEFAULT_INTEGRITY)))
            .andExpect(jsonPath("$.critical").value(DEFAULT_CRITICAL.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAvailability() throws Exception {
        // Get the availability
        restAvailabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability
        Availability updatedAvailability = availabilityRepository.findById(availability.getId()).get();
        // Disconnect from session so that the updates on updatedAvailability are not directly saved in db
        em.detach(updatedAvailability);
        updatedAvailability
            .financial(UPDATED_FINANCIAL)
            .legal(UPDATED_LEGAL)
            .image(UPDATED_IMAGE)
            .strategy(UPDATED_STRATEGY)
            .operational(UPDATED_OPERATIONAL)
            .traceability(UPDATED_TRACEABILITY)
            .traceabilityContentType(UPDATED_TRACEABILITY_CONTENT_TYPE)
            .confidentiality(UPDATED_CONFIDENTIALITY)
            .confidentialityContentType(UPDATED_CONFIDENTIALITY_CONTENT_TYPE)
            .integrity(UPDATED_INTEGRITY)
            .integrityContentType(UPDATED_INTEGRITY_CONTENT_TYPE)
            .critical(UPDATED_CRITICAL);

        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvailability.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getFinancial()).isEqualTo(UPDATED_FINANCIAL);
        assertThat(testAvailability.getLegal()).isEqualTo(UPDATED_LEGAL);
        assertThat(testAvailability.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAvailability.getStrategy()).isEqualTo(UPDATED_STRATEGY);
        assertThat(testAvailability.getOperational()).isEqualTo(UPDATED_OPERATIONAL);
        assertThat(testAvailability.getTraceability()).isEqualTo(UPDATED_TRACEABILITY);
        assertThat(testAvailability.getTraceabilityContentType()).isEqualTo(UPDATED_TRACEABILITY_CONTENT_TYPE);
        assertThat(testAvailability.getConfidentiality()).isEqualTo(UPDATED_CONFIDENTIALITY);
        assertThat(testAvailability.getConfidentialityContentType()).isEqualTo(UPDATED_CONFIDENTIALITY_CONTENT_TYPE);
        assertThat(testAvailability.getIntegrity()).isEqualTo(UPDATED_INTEGRITY);
        assertThat(testAvailability.getIntegrityContentType()).isEqualTo(UPDATED_INTEGRITY_CONTENT_TYPE);
        assertThat(testAvailability.getCritical()).isEqualTo(UPDATED_CRITICAL);
    }

    @Test
    @Transactional
    void putNonExistingAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, availability.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(availability)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvailabilityWithPatch() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability using partial update
        Availability partialUpdatedAvailability = new Availability();
        partialUpdatedAvailability.setId(availability.getId());

        partialUpdatedAvailability
            .financial(UPDATED_FINANCIAL)
            .image(UPDATED_IMAGE)
            .strategy(UPDATED_STRATEGY)
            .operational(UPDATED_OPERATIONAL);

        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getFinancial()).isEqualTo(UPDATED_FINANCIAL);
        assertThat(testAvailability.getLegal()).isEqualTo(DEFAULT_LEGAL);
        assertThat(testAvailability.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAvailability.getStrategy()).isEqualTo(UPDATED_STRATEGY);
        assertThat(testAvailability.getOperational()).isEqualTo(UPDATED_OPERATIONAL);
        assertThat(testAvailability.getTraceability()).isEqualTo(DEFAULT_TRACEABILITY);
        assertThat(testAvailability.getTraceabilityContentType()).isEqualTo(DEFAULT_TRACEABILITY_CONTENT_TYPE);
        assertThat(testAvailability.getConfidentiality()).isEqualTo(DEFAULT_CONFIDENTIALITY);
        assertThat(testAvailability.getConfidentialityContentType()).isEqualTo(DEFAULT_CONFIDENTIALITY_CONTENT_TYPE);
        assertThat(testAvailability.getIntegrity()).isEqualTo(DEFAULT_INTEGRITY);
        assertThat(testAvailability.getIntegrityContentType()).isEqualTo(DEFAULT_INTEGRITY_CONTENT_TYPE);
        assertThat(testAvailability.getCritical()).isEqualTo(DEFAULT_CRITICAL);
    }

    @Test
    @Transactional
    void fullUpdateAvailabilityWithPatch() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability using partial update
        Availability partialUpdatedAvailability = new Availability();
        partialUpdatedAvailability.setId(availability.getId());

        partialUpdatedAvailability
            .financial(UPDATED_FINANCIAL)
            .legal(UPDATED_LEGAL)
            .image(UPDATED_IMAGE)
            .strategy(UPDATED_STRATEGY)
            .operational(UPDATED_OPERATIONAL)
            .traceability(UPDATED_TRACEABILITY)
            .traceabilityContentType(UPDATED_TRACEABILITY_CONTENT_TYPE)
            .confidentiality(UPDATED_CONFIDENTIALITY)
            .confidentialityContentType(UPDATED_CONFIDENTIALITY_CONTENT_TYPE)
            .integrity(UPDATED_INTEGRITY)
            .integrityContentType(UPDATED_INTEGRITY_CONTENT_TYPE)
            .critical(UPDATED_CRITICAL);

        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvailability))
            )
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getFinancial()).isEqualTo(UPDATED_FINANCIAL);
        assertThat(testAvailability.getLegal()).isEqualTo(UPDATED_LEGAL);
        assertThat(testAvailability.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAvailability.getStrategy()).isEqualTo(UPDATED_STRATEGY);
        assertThat(testAvailability.getOperational()).isEqualTo(UPDATED_OPERATIONAL);
        assertThat(testAvailability.getTraceability()).isEqualTo(UPDATED_TRACEABILITY);
        assertThat(testAvailability.getTraceabilityContentType()).isEqualTo(UPDATED_TRACEABILITY_CONTENT_TYPE);
        assertThat(testAvailability.getConfidentiality()).isEqualTo(UPDATED_CONFIDENTIALITY);
        assertThat(testAvailability.getConfidentialityContentType()).isEqualTo(UPDATED_CONFIDENTIALITY_CONTENT_TYPE);
        assertThat(testAvailability.getIntegrity()).isEqualTo(UPDATED_INTEGRITY);
        assertThat(testAvailability.getIntegrityContentType()).isEqualTo(UPDATED_INTEGRITY_CONTENT_TYPE);
        assertThat(testAvailability.getCritical()).isEqualTo(UPDATED_CRITICAL);
    }

    @Test
    @Transactional
    void patchNonExistingAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, availability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();
        availability.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(availability))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        int databaseSizeBeforeDelete = availabilityRepository.findAll().size();

        // Delete the availability
        restAvailabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, availability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
