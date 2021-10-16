package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.TripCategory;
import com.maria.trip.repository.TripCategoryRepository;
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
 * Integration tests for the {@link TripCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripCategoryResourceIT {

    private static final String DEFAULT_CATEGORYNAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORYNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trip-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripCategoryRepository tripCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripCategoryMockMvc;

    private TripCategory tripCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripCategory createEntity(EntityManager em) {
        TripCategory tripCategory = new TripCategory().categoryname(DEFAULT_CATEGORYNAME);
        return tripCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripCategory createUpdatedEntity(EntityManager em) {
        TripCategory tripCategory = new TripCategory().categoryname(UPDATED_CATEGORYNAME);
        return tripCategory;
    }

    @BeforeEach
    public void initTest() {
        tripCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createTripCategory() throws Exception {
        int databaseSizeBeforeCreate = tripCategoryRepository.findAll().size();
        // Create the TripCategory
        restTripCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripCategory)))
            .andExpect(status().isCreated());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        TripCategory testTripCategory = tripCategoryList.get(tripCategoryList.size() - 1);
        assertThat(testTripCategory.getCategoryname()).isEqualTo(DEFAULT_CATEGORYNAME);
    }

    @Test
    @Transactional
    void createTripCategoryWithExistingId() throws Exception {
        // Create the TripCategory with an existing ID
        tripCategory.setId(1L);

        int databaseSizeBeforeCreate = tripCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripCategory)))
            .andExpect(status().isBadRequest());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripCategories() throws Exception {
        // Initialize the database
        tripCategoryRepository.saveAndFlush(tripCategory);

        // Get all the tripCategoryList
        restTripCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryname").value(hasItem(DEFAULT_CATEGORYNAME)));
    }

    @Test
    @Transactional
    void getTripCategory() throws Exception {
        // Initialize the database
        tripCategoryRepository.saveAndFlush(tripCategory);

        // Get the tripCategory
        restTripCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, tripCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryname").value(DEFAULT_CATEGORYNAME));
    }

    @Test
    @Transactional
    void getNonExistingTripCategory() throws Exception {
        // Get the tripCategory
        restTripCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTripCategory() throws Exception {
        // Initialize the database
        tripCategoryRepository.saveAndFlush(tripCategory);

        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();

        // Update the tripCategory
        TripCategory updatedTripCategory = tripCategoryRepository.findById(tripCategory.getId()).get();
        // Disconnect from session so that the updates on updatedTripCategory are not directly saved in db
        em.detach(updatedTripCategory);
        updatedTripCategory.categoryname(UPDATED_CATEGORYNAME);

        restTripCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTripCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTripCategory))
            )
            .andExpect(status().isOk());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
        TripCategory testTripCategory = tripCategoryList.get(tripCategoryList.size() - 1);
        assertThat(testTripCategory.getCategoryname()).isEqualTo(UPDATED_CATEGORYNAME);
    }

    @Test
    @Transactional
    void putNonExistingTripCategory() throws Exception {
        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();
        tripCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripCategory() throws Exception {
        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();
        tripCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripCategory() throws Exception {
        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();
        tripCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripCategoryWithPatch() throws Exception {
        // Initialize the database
        tripCategoryRepository.saveAndFlush(tripCategory);

        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();

        // Update the tripCategory using partial update
        TripCategory partialUpdatedTripCategory = new TripCategory();
        partialUpdatedTripCategory.setId(tripCategory.getId());

        restTripCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripCategory))
            )
            .andExpect(status().isOk());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
        TripCategory testTripCategory = tripCategoryList.get(tripCategoryList.size() - 1);
        assertThat(testTripCategory.getCategoryname()).isEqualTo(DEFAULT_CATEGORYNAME);
    }

    @Test
    @Transactional
    void fullUpdateTripCategoryWithPatch() throws Exception {
        // Initialize the database
        tripCategoryRepository.saveAndFlush(tripCategory);

        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();

        // Update the tripCategory using partial update
        TripCategory partialUpdatedTripCategory = new TripCategory();
        partialUpdatedTripCategory.setId(tripCategory.getId());

        partialUpdatedTripCategory.categoryname(UPDATED_CATEGORYNAME);

        restTripCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripCategory))
            )
            .andExpect(status().isOk());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
        TripCategory testTripCategory = tripCategoryList.get(tripCategoryList.size() - 1);
        assertThat(testTripCategory.getCategoryname()).isEqualTo(UPDATED_CATEGORYNAME);
    }

    @Test
    @Transactional
    void patchNonExistingTripCategory() throws Exception {
        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();
        tripCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripCategory() throws Exception {
        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();
        tripCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripCategory() throws Exception {
        int databaseSizeBeforeUpdate = tripCategoryRepository.findAll().size();
        tripCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripCategory in the database
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripCategory() throws Exception {
        // Initialize the database
        tripCategoryRepository.saveAndFlush(tripCategory);

        int databaseSizeBeforeDelete = tripCategoryRepository.findAll().size();

        // Delete the tripCategory
        restTripCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TripCategory> tripCategoryList = tripCategoryRepository.findAll();
        assertThat(tripCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
