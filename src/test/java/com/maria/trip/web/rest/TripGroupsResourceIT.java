package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.TripGroups;
import com.maria.trip.repository.TripGroupsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TripGroupsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TripGroupsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trip-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripGroupsRepository tripGroupsRepository;

    @Mock
    private TripGroupsRepository tripGroupsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripGroupsMockMvc;

    private TripGroups tripGroups;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripGroups createEntity(EntityManager em) {
        TripGroups tripGroups = new TripGroups().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return tripGroups;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripGroups createUpdatedEntity(EntityManager em) {
        TripGroups tripGroups = new TripGroups().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return tripGroups;
    }

    @BeforeEach
    public void initTest() {
        tripGroups = createEntity(em);
    }

    @Test
    @Transactional
    void createTripGroups() throws Exception {
        int databaseSizeBeforeCreate = tripGroupsRepository.findAll().size();
        // Create the TripGroups
        restTripGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripGroups)))
            .andExpect(status().isCreated());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeCreate + 1);
        TripGroups testTripGroups = tripGroupsList.get(tripGroupsList.size() - 1);
        assertThat(testTripGroups.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTripGroups.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTripGroupsWithExistingId() throws Exception {
        // Create the TripGroups with an existing ID
        tripGroups.setId(1L);

        int databaseSizeBeforeCreate = tripGroupsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripGroups)))
            .andExpect(status().isBadRequest());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripGroups() throws Exception {
        // Initialize the database
        tripGroupsRepository.saveAndFlush(tripGroups);

        // Get all the tripGroupsList
        restTripGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTripGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(tripGroupsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTripGroupsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tripGroupsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTripGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tripGroupsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTripGroupsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tripGroupsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTripGroups() throws Exception {
        // Initialize the database
        tripGroupsRepository.saveAndFlush(tripGroups);

        // Get the tripGroups
        restTripGroupsMockMvc
            .perform(get(ENTITY_API_URL_ID, tripGroups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripGroups.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTripGroups() throws Exception {
        // Get the tripGroups
        restTripGroupsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTripGroups() throws Exception {
        // Initialize the database
        tripGroupsRepository.saveAndFlush(tripGroups);

        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();

        // Update the tripGroups
        TripGroups updatedTripGroups = tripGroupsRepository.findById(tripGroups.getId()).get();
        // Disconnect from session so that the updates on updatedTripGroups are not directly saved in db
        em.detach(updatedTripGroups);
        updatedTripGroups.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTripGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTripGroups.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTripGroups))
            )
            .andExpect(status().isOk());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
        TripGroups testTripGroups = tripGroupsList.get(tripGroupsList.size() - 1);
        assertThat(testTripGroups.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTripGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTripGroups() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();
        tripGroups.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripGroups.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripGroups))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripGroups() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();
        tripGroups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripGroups))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripGroups() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();
        tripGroups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripGroups)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripGroupsWithPatch() throws Exception {
        // Initialize the database
        tripGroupsRepository.saveAndFlush(tripGroups);

        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();

        // Update the tripGroups using partial update
        TripGroups partialUpdatedTripGroups = new TripGroups();
        partialUpdatedTripGroups.setId(tripGroups.getId());

        partialUpdatedTripGroups.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTripGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripGroups))
            )
            .andExpect(status().isOk());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
        TripGroups testTripGroups = tripGroupsList.get(tripGroupsList.size() - 1);
        assertThat(testTripGroups.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTripGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTripGroupsWithPatch() throws Exception {
        // Initialize the database
        tripGroupsRepository.saveAndFlush(tripGroups);

        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();

        // Update the tripGroups using partial update
        TripGroups partialUpdatedTripGroups = new TripGroups();
        partialUpdatedTripGroups.setId(tripGroups.getId());

        partialUpdatedTripGroups.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restTripGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripGroups))
            )
            .andExpect(status().isOk());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
        TripGroups testTripGroups = tripGroupsList.get(tripGroupsList.size() - 1);
        assertThat(testTripGroups.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTripGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTripGroups() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();
        tripGroups.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripGroups))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripGroups() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();
        tripGroups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripGroups))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripGroups() throws Exception {
        int databaseSizeBeforeUpdate = tripGroupsRepository.findAll().size();
        tripGroups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripGroups))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripGroups in the database
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripGroups() throws Exception {
        // Initialize the database
        tripGroupsRepository.saveAndFlush(tripGroups);

        int databaseSizeBeforeDelete = tripGroupsRepository.findAll().size();

        // Delete the tripGroups
        restTripGroupsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripGroups.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TripGroups> tripGroupsList = tripGroupsRepository.findAll();
        assertThat(tripGroupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
