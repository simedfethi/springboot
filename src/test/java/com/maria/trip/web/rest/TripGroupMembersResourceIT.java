package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.TripGroupMembers;
import com.maria.trip.repository.TripGroupMembersRepository;
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
 * Integration tests for the {@link TripGroupMembersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripGroupMembersResourceIT {

    private static final String ENTITY_API_URL = "/api/trip-group-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripGroupMembersRepository tripGroupMembersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripGroupMembersMockMvc;

    private TripGroupMembers tripGroupMembers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripGroupMembers createEntity(EntityManager em) {
        TripGroupMembers tripGroupMembers = new TripGroupMembers();
        return tripGroupMembers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripGroupMembers createUpdatedEntity(EntityManager em) {
        TripGroupMembers tripGroupMembers = new TripGroupMembers();
        return tripGroupMembers;
    }

    @BeforeEach
    public void initTest() {
        tripGroupMembers = createEntity(em);
    }

    @Test
    @Transactional
    void createTripGroupMembers() throws Exception {
        int databaseSizeBeforeCreate = tripGroupMembersRepository.findAll().size();
        // Create the TripGroupMembers
        restTripGroupMembersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isCreated());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeCreate + 1);
        TripGroupMembers testTripGroupMembers = tripGroupMembersList.get(tripGroupMembersList.size() - 1);
    }

    @Test
    @Transactional
    void createTripGroupMembersWithExistingId() throws Exception {
        // Create the TripGroupMembers with an existing ID
        tripGroupMembers.setId(1L);

        int databaseSizeBeforeCreate = tripGroupMembersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripGroupMembersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripGroupMembers() throws Exception {
        // Initialize the database
        tripGroupMembersRepository.saveAndFlush(tripGroupMembers);

        // Get all the tripGroupMembersList
        restTripGroupMembersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripGroupMembers.getId().intValue())));
    }

    @Test
    @Transactional
    void getTripGroupMembers() throws Exception {
        // Initialize the database
        tripGroupMembersRepository.saveAndFlush(tripGroupMembers);

        // Get the tripGroupMembers
        restTripGroupMembersMockMvc
            .perform(get(ENTITY_API_URL_ID, tripGroupMembers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripGroupMembers.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTripGroupMembers() throws Exception {
        // Get the tripGroupMembers
        restTripGroupMembersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTripGroupMembers() throws Exception {
        // Initialize the database
        tripGroupMembersRepository.saveAndFlush(tripGroupMembers);

        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();

        // Update the tripGroupMembers
        TripGroupMembers updatedTripGroupMembers = tripGroupMembersRepository.findById(tripGroupMembers.getId()).get();
        // Disconnect from session so that the updates on updatedTripGroupMembers are not directly saved in db
        em.detach(updatedTripGroupMembers);

        restTripGroupMembersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTripGroupMembers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTripGroupMembers))
            )
            .andExpect(status().isOk());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
        TripGroupMembers testTripGroupMembers = tripGroupMembersList.get(tripGroupMembersList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingTripGroupMembers() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();
        tripGroupMembers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripGroupMembersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripGroupMembers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripGroupMembers() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();
        tripGroupMembers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupMembersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripGroupMembers() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();
        tripGroupMembers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupMembersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripGroupMembersWithPatch() throws Exception {
        // Initialize the database
        tripGroupMembersRepository.saveAndFlush(tripGroupMembers);

        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();

        // Update the tripGroupMembers using partial update
        TripGroupMembers partialUpdatedTripGroupMembers = new TripGroupMembers();
        partialUpdatedTripGroupMembers.setId(tripGroupMembers.getId());

        restTripGroupMembersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripGroupMembers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripGroupMembers))
            )
            .andExpect(status().isOk());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
        TripGroupMembers testTripGroupMembers = tripGroupMembersList.get(tripGroupMembersList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateTripGroupMembersWithPatch() throws Exception {
        // Initialize the database
        tripGroupMembersRepository.saveAndFlush(tripGroupMembers);

        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();

        // Update the tripGroupMembers using partial update
        TripGroupMembers partialUpdatedTripGroupMembers = new TripGroupMembers();
        partialUpdatedTripGroupMembers.setId(tripGroupMembers.getId());

        restTripGroupMembersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripGroupMembers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripGroupMembers))
            )
            .andExpect(status().isOk());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
        TripGroupMembers testTripGroupMembers = tripGroupMembersList.get(tripGroupMembersList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingTripGroupMembers() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();
        tripGroupMembers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripGroupMembersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripGroupMembers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripGroupMembers() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();
        tripGroupMembers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupMembersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripGroupMembers() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupMembersRepository.findAll().size();
        tripGroupMembers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupMembersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripGroupMembers))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripGroupMembers in the database
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripGroupMembers() throws Exception {
        // Initialize the database
        tripGroupMembersRepository.saveAndFlush(tripGroupMembers);

        int databaseSizeBeforeDelete = tripGroupMembersRepository.findAll().size();

        // Delete the tripGroupMembers
        restTripGroupMembersMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripGroupMembers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TripGroupMembers> tripGroupMembersList = tripGroupMembersRepository.findAll();
        assertThat(tripGroupMembersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
