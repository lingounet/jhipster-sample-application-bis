package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HostingType;
import com.mycompany.myapp.repository.HostingTypeRepository;
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
 * Integration tests for the {@link HostingTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HostingTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hosting-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HostingTypeRepository hostingTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHostingTypeMockMvc;

    private HostingType hostingType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HostingType createEntity(EntityManager em) {
        HostingType hostingType = new HostingType().name(DEFAULT_NAME);
        return hostingType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HostingType createUpdatedEntity(EntityManager em) {
        HostingType hostingType = new HostingType().name(UPDATED_NAME);
        return hostingType;
    }

    @BeforeEach
    public void initTest() {
        hostingType = createEntity(em);
    }

    @Test
    @Transactional
    void createHostingType() throws Exception {
        int databaseSizeBeforeCreate = hostingTypeRepository.findAll().size();
        // Create the HostingType
        restHostingTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostingType)))
            .andExpect(status().isCreated());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeCreate + 1);
        HostingType testHostingType = hostingTypeList.get(hostingTypeList.size() - 1);
        assertThat(testHostingType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createHostingTypeWithExistingId() throws Exception {
        // Create the HostingType with an existing ID
        hostingType.setId(1L);

        int databaseSizeBeforeCreate = hostingTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostingTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostingType)))
            .andExpect(status().isBadRequest());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHostingTypes() throws Exception {
        // Initialize the database
        hostingTypeRepository.saveAndFlush(hostingType);

        // Get all the hostingTypeList
        restHostingTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hostingType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getHostingType() throws Exception {
        // Initialize the database
        hostingTypeRepository.saveAndFlush(hostingType);

        // Get the hostingType
        restHostingTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, hostingType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hostingType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHostingType() throws Exception {
        // Get the hostingType
        restHostingTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHostingType() throws Exception {
        // Initialize the database
        hostingTypeRepository.saveAndFlush(hostingType);

        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();

        // Update the hostingType
        HostingType updatedHostingType = hostingTypeRepository.findById(hostingType.getId()).get();
        // Disconnect from session so that the updates on updatedHostingType are not directly saved in db
        em.detach(updatedHostingType);
        updatedHostingType.name(UPDATED_NAME);

        restHostingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHostingType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHostingType))
            )
            .andExpect(status().isOk());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
        HostingType testHostingType = hostingTypeList.get(hostingTypeList.size() - 1);
        assertThat(testHostingType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHostingType() throws Exception {
        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();
        hostingType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hostingType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHostingType() throws Exception {
        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();
        hostingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHostingType() throws Exception {
        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();
        hostingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostingType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHostingTypeWithPatch() throws Exception {
        // Initialize the database
        hostingTypeRepository.saveAndFlush(hostingType);

        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();

        // Update the hostingType using partial update
        HostingType partialUpdatedHostingType = new HostingType();
        partialUpdatedHostingType.setId(hostingType.getId());

        partialUpdatedHostingType.name(UPDATED_NAME);

        restHostingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHostingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHostingType))
            )
            .andExpect(status().isOk());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
        HostingType testHostingType = hostingTypeList.get(hostingTypeList.size() - 1);
        assertThat(testHostingType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHostingTypeWithPatch() throws Exception {
        // Initialize the database
        hostingTypeRepository.saveAndFlush(hostingType);

        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();

        // Update the hostingType using partial update
        HostingType partialUpdatedHostingType = new HostingType();
        partialUpdatedHostingType.setId(hostingType.getId());

        partialUpdatedHostingType.name(UPDATED_NAME);

        restHostingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHostingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHostingType))
            )
            .andExpect(status().isOk());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
        HostingType testHostingType = hostingTypeList.get(hostingTypeList.size() - 1);
        assertThat(testHostingType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHostingType() throws Exception {
        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();
        hostingType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hostingType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHostingType() throws Exception {
        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();
        hostingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostingType))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHostingType() throws Exception {
        int databaseSizeBeforeUpdate = hostingTypeRepository.findAll().size();
        hostingType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hostingType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HostingType in the database
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHostingType() throws Exception {
        // Initialize the database
        hostingTypeRepository.saveAndFlush(hostingType);

        int databaseSizeBeforeDelete = hostingTypeRepository.findAll().size();

        // Delete the hostingType
        restHostingTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, hostingType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HostingType> hostingTypeList = hostingTypeRepository.findAll();
        assertThat(hostingTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
