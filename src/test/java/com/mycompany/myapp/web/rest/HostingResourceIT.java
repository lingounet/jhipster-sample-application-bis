package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Hosting;
import com.mycompany.myapp.repository.HostingRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link HostingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HostingResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/hostings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HostingRepository hostingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHostingMockMvc;

    private Hosting hosting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hosting createEntity(EntityManager em) {
        Hosting hosting = new Hosting().date(DEFAULT_DATE);
        return hosting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hosting createUpdatedEntity(EntityManager em) {
        Hosting hosting = new Hosting().date(UPDATED_DATE);
        return hosting;
    }

    @BeforeEach
    public void initTest() {
        hosting = createEntity(em);
    }

    @Test
    @Transactional
    void createHosting() throws Exception {
        int databaseSizeBeforeCreate = hostingRepository.findAll().size();
        // Create the Hosting
        restHostingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hosting)))
            .andExpect(status().isCreated());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeCreate + 1);
        Hosting testHosting = hostingList.get(hostingList.size() - 1);
        assertThat(testHosting.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createHostingWithExistingId() throws Exception {
        // Create the Hosting with an existing ID
        hosting.setId(1L);

        int databaseSizeBeforeCreate = hostingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hosting)))
            .andExpect(status().isBadRequest());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHostings() throws Exception {
        // Initialize the database
        hostingRepository.saveAndFlush(hosting);

        // Get all the hostingList
        restHostingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hosting.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getHosting() throws Exception {
        // Initialize the database
        hostingRepository.saveAndFlush(hosting);

        // Get the hosting
        restHostingMockMvc
            .perform(get(ENTITY_API_URL_ID, hosting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hosting.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHosting() throws Exception {
        // Get the hosting
        restHostingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHosting() throws Exception {
        // Initialize the database
        hostingRepository.saveAndFlush(hosting);

        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();

        // Update the hosting
        Hosting updatedHosting = hostingRepository.findById(hosting.getId()).get();
        // Disconnect from session so that the updates on updatedHosting are not directly saved in db
        em.detach(updatedHosting);
        updatedHosting.date(UPDATED_DATE);

        restHostingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHosting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHosting))
            )
            .andExpect(status().isOk());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
        Hosting testHosting = hostingList.get(hostingList.size() - 1);
        assertThat(testHosting.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingHosting() throws Exception {
        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();
        hosting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hosting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hosting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHosting() throws Exception {
        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();
        hosting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hosting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHosting() throws Exception {
        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();
        hosting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hosting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHostingWithPatch() throws Exception {
        // Initialize the database
        hostingRepository.saveAndFlush(hosting);

        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();

        // Update the hosting using partial update
        Hosting partialUpdatedHosting = new Hosting();
        partialUpdatedHosting.setId(hosting.getId());

        restHostingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHosting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHosting))
            )
            .andExpect(status().isOk());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
        Hosting testHosting = hostingList.get(hostingList.size() - 1);
        assertThat(testHosting.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateHostingWithPatch() throws Exception {
        // Initialize the database
        hostingRepository.saveAndFlush(hosting);

        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();

        // Update the hosting using partial update
        Hosting partialUpdatedHosting = new Hosting();
        partialUpdatedHosting.setId(hosting.getId());

        partialUpdatedHosting.date(UPDATED_DATE);

        restHostingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHosting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHosting))
            )
            .andExpect(status().isOk());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
        Hosting testHosting = hostingList.get(hostingList.size() - 1);
        assertThat(testHosting.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingHosting() throws Exception {
        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();
        hosting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hosting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hosting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHosting() throws Exception {
        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();
        hosting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hosting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHosting() throws Exception {
        int databaseSizeBeforeUpdate = hostingRepository.findAll().size();
        hosting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHostingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hosting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hosting in the database
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHosting() throws Exception {
        // Initialize the database
        hostingRepository.saveAndFlush(hosting);

        int databaseSizeBeforeDelete = hostingRepository.findAll().size();

        // Delete the hosting
        restHostingMockMvc
            .perform(delete(ENTITY_API_URL_ID, hosting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hosting> hostingList = hostingRepository.findAll();
        assertThat(hostingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
