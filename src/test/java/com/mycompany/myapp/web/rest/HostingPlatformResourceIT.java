package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HostingPlatform;
import com.mycompany.myapp.repository.HostingPlatformRepository;
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
 * Integration tests for the {@link HostingPlatformResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HostingPlatformResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hosting-platforms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HostingPlatformRepository hostingPlatformRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHostingPlatformMockMvc;

    private HostingPlatform hostingPlatform;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HostingPlatform createEntity(EntityManager em) {
        HostingPlatform hostingPlatform = new HostingPlatform().name(DEFAULT_NAME);
        return hostingPlatform;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HostingPlatform createUpdatedEntity(EntityManager em) {
        HostingPlatform hostingPlatform = new HostingPlatform().name(UPDATED_NAME);
        return hostingPlatform;
    }

    @BeforeEach
    public void initTest() {
        hostingPlatform = createEntity(em);
    }

    @Test
    @Transactional
    void createHostingPlatform() throws Exception {
        int databaseSizeBeforeCreate = hostingPlatformRepository.findAll().size();
        // Create the HostingPlatform
        restHostingPlatformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isCreated());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeCreate + 1);
        HostingPlatform testHostingPlatform = hostingPlatformList.get(hostingPlatformList.size() - 1);
        assertThat(testHostingPlatform.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createHostingPlatformWithExistingId() throws Exception {
        // Create the HostingPlatform with an existing ID
        hostingPlatform.setId(1L);

        int databaseSizeBeforeCreate = hostingPlatformRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostingPlatformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHostingPlatforms() throws Exception {
        // Initialize the database
        hostingPlatformRepository.saveAndFlush(hostingPlatform);

        // Get all the hostingPlatformList
        restHostingPlatformMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hostingPlatform.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getHostingPlatform() throws Exception {
        // Initialize the database
        hostingPlatformRepository.saveAndFlush(hostingPlatform);

        // Get the hostingPlatform
        restHostingPlatformMockMvc
            .perform(get(ENTITY_API_URL_ID, hostingPlatform.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hostingPlatform.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingHostingPlatform() throws Exception {
        // Get the hostingPlatform
        restHostingPlatformMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHostingPlatform() throws Exception {
        // Initialize the database
        hostingPlatformRepository.saveAndFlush(hostingPlatform);

        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();

        // Update the hostingPlatform
        HostingPlatform updatedHostingPlatform = hostingPlatformRepository.findById(hostingPlatform.getId()).get();
        // Disconnect from session so that the updates on updatedHostingPlatform are not directly saved in db
        em.detach(updatedHostingPlatform);
        updatedHostingPlatform.name(UPDATED_NAME);

        restHostingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHostingPlatform.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHostingPlatform))
            )
            .andExpect(status().isOk());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
        HostingPlatform testHostingPlatform = hostingPlatformList.get(hostingPlatformList.size() - 1);
        assertThat(testHostingPlatform.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHostingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();
        hostingPlatform.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hostingPlatform.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHostingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();
        hostingPlatform.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHostingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();
        hostingPlatform.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingPlatformMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHostingPlatformWithPatch() throws Exception {
        // Initialize the database
        hostingPlatformRepository.saveAndFlush(hostingPlatform);

        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();

        // Update the hostingPlatform using partial update
        HostingPlatform partialUpdatedHostingPlatform = new HostingPlatform();
        partialUpdatedHostingPlatform.setId(hostingPlatform.getId());

        restHostingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHostingPlatform.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHostingPlatform))
            )
            .andExpect(status().isOk());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
        HostingPlatform testHostingPlatform = hostingPlatformList.get(hostingPlatformList.size() - 1);
        assertThat(testHostingPlatform.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHostingPlatformWithPatch() throws Exception {
        // Initialize the database
        hostingPlatformRepository.saveAndFlush(hostingPlatform);

        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();

        // Update the hostingPlatform using partial update
        HostingPlatform partialUpdatedHostingPlatform = new HostingPlatform();
        partialUpdatedHostingPlatform.setId(hostingPlatform.getId());

        partialUpdatedHostingPlatform.name(UPDATED_NAME);

        restHostingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHostingPlatform.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHostingPlatform))
            )
            .andExpect(status().isOk());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
        HostingPlatform testHostingPlatform = hostingPlatformList.get(hostingPlatformList.size() - 1);
        assertThat(testHostingPlatform.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHostingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();
        hostingPlatform.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hostingPlatform.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHostingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();
        hostingPlatform.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isBadRequest());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHostingPlatform() throws Exception {
        int databaseSizeBeforeUpdate = hostingPlatformRepository.findAll().size();
        hostingPlatform.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingPlatformMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hostingPlatform))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HostingPlatform in the database
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHostingPlatform() throws Exception {
        // Initialize the database
        hostingPlatformRepository.saveAndFlush(hostingPlatform);

        int databaseSizeBeforeDelete = hostingPlatformRepository.findAll().size();

        // Delete the hostingPlatform
        restHostingPlatformMockMvc
            .perform(delete(ENTITY_API_URL_ID, hostingPlatform.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HostingPlatform> hostingPlatformList = hostingPlatformRepository.findAll();
        assertThat(hostingPlatformList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
