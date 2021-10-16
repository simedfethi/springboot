package com.maria.trip.web.rest;

import static com.maria.trip.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.Tripdetail;
import com.maria.trip.repository.TripdetailRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link TripdetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripdetailResourceIT {

    private static final Integer DEFAULT_MINIMUM_LIST = 1;
    private static final Integer UPDATED_MINIMUM_LIST = 2;

    private static final Integer DEFAULT_MAXIMUM_LIST = 1;
    private static final Integer UPDATED_MAXIMUM_LIST = 2;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LASTUPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LASTUPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DEPARTURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPARTURE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ARRIVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ARRIVAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_CONTENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/tripdetails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripdetailRepository tripdetailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripdetailMockMvc;

    private Tripdetail tripdetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tripdetail createEntity(EntityManager em) {
        Tripdetail tripdetail = new Tripdetail()
            .minimumList(DEFAULT_MINIMUM_LIST)
            .maximumList(DEFAULT_MAXIMUM_LIST)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastupdated(DEFAULT_LASTUPDATED)
            .departureDate(DEFAULT_DEPARTURE_DATE)
            .arrivalDate(DEFAULT_ARRIVAL_DATE)
            .contentDate(DEFAULT_CONTENT_DATE);
        return tripdetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tripdetail createUpdatedEntity(EntityManager em) {
        Tripdetail tripdetail = new Tripdetail()
            .minimumList(UPDATED_MINIMUM_LIST)
            .maximumList(UPDATED_MAXIMUM_LIST)
            .createdDate(UPDATED_CREATED_DATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .contentDate(UPDATED_CONTENT_DATE);
        return tripdetail;
    }

    @BeforeEach
    public void initTest() {
        tripdetail = createEntity(em);
    }

    @Test
    @Transactional
    void createTripdetail() throws Exception {
        int databaseSizeBeforeCreate = tripdetailRepository.findAll().size();
        // Create the Tripdetail
        restTripdetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripdetail)))
            .andExpect(status().isCreated());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeCreate + 1);
        Tripdetail testTripdetail = tripdetailList.get(tripdetailList.size() - 1);
        assertThat(testTripdetail.getMinimumList()).isEqualTo(DEFAULT_MINIMUM_LIST);
        assertThat(testTripdetail.getMaximumList()).isEqualTo(DEFAULT_MAXIMUM_LIST);
        assertThat(testTripdetail.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTripdetail.getLastupdated()).isEqualTo(DEFAULT_LASTUPDATED);
        assertThat(testTripdetail.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testTripdetail.getArrivalDate()).isEqualTo(DEFAULT_ARRIVAL_DATE);
        assertThat(testTripdetail.getContentDate()).isEqualTo(DEFAULT_CONTENT_DATE);
    }

    @Test
    @Transactional
    void createTripdetailWithExistingId() throws Exception {
        // Create the Tripdetail with an existing ID
        tripdetail.setId(1L);

        int databaseSizeBeforeCreate = tripdetailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripdetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripdetail)))
            .andExpect(status().isBadRequest());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripdetails() throws Exception {
        // Initialize the database
        tripdetailRepository.saveAndFlush(tripdetail);

        // Get all the tripdetailList
        restTripdetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripdetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumList").value(hasItem(DEFAULT_MINIMUM_LIST)))
            .andExpect(jsonPath("$.[*].maximumList").value(hasItem(DEFAULT_MAXIMUM_LIST)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())))
            .andExpect(jsonPath("$.[*].departureDate").value(hasItem(DEFAULT_DEPARTURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].arrivalDate").value(hasItem(DEFAULT_ARRIVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].contentDate").value(hasItem(sameInstant(DEFAULT_CONTENT_DATE))));
    }

    @Test
    @Transactional
    void getTripdetail() throws Exception {
        // Initialize the database
        tripdetailRepository.saveAndFlush(tripdetail);

        // Get the tripdetail
        restTripdetailMockMvc
            .perform(get(ENTITY_API_URL_ID, tripdetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripdetail.getId().intValue()))
            .andExpect(jsonPath("$.minimumList").value(DEFAULT_MINIMUM_LIST))
            .andExpect(jsonPath("$.maximumList").value(DEFAULT_MAXIMUM_LIST))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastupdated").value(DEFAULT_LASTUPDATED.toString()))
            .andExpect(jsonPath("$.departureDate").value(DEFAULT_DEPARTURE_DATE.toString()))
            .andExpect(jsonPath("$.arrivalDate").value(DEFAULT_ARRIVAL_DATE.toString()))
            .andExpect(jsonPath("$.contentDate").value(sameInstant(DEFAULT_CONTENT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingTripdetail() throws Exception {
        // Get the tripdetail
        restTripdetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTripdetail() throws Exception {
        // Initialize the database
        tripdetailRepository.saveAndFlush(tripdetail);

        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();

        // Update the tripdetail
        Tripdetail updatedTripdetail = tripdetailRepository.findById(tripdetail.getId()).get();
        // Disconnect from session so that the updates on updatedTripdetail are not directly saved in db
        em.detach(updatedTripdetail);
        updatedTripdetail
            .minimumList(UPDATED_MINIMUM_LIST)
            .maximumList(UPDATED_MAXIMUM_LIST)
            .createdDate(UPDATED_CREATED_DATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .contentDate(UPDATED_CONTENT_DATE);

        restTripdetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTripdetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTripdetail))
            )
            .andExpect(status().isOk());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
        Tripdetail testTripdetail = tripdetailList.get(tripdetailList.size() - 1);
        assertThat(testTripdetail.getMinimumList()).isEqualTo(UPDATED_MINIMUM_LIST);
        assertThat(testTripdetail.getMaximumList()).isEqualTo(UPDATED_MAXIMUM_LIST);
        assertThat(testTripdetail.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTripdetail.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testTripdetail.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testTripdetail.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
        assertThat(testTripdetail.getContentDate()).isEqualTo(UPDATED_CONTENT_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTripdetail() throws Exception {
        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();
        tripdetail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripdetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripdetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripdetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripdetail() throws Exception {
        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();
        tripdetail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripdetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripdetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripdetail() throws Exception {
        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();
        tripdetail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripdetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripdetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripdetailWithPatch() throws Exception {
        // Initialize the database
        tripdetailRepository.saveAndFlush(tripdetail);

        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();

        // Update the tripdetail using partial update
        Tripdetail partialUpdatedTripdetail = new Tripdetail();
        partialUpdatedTripdetail.setId(tripdetail.getId());

        partialUpdatedTripdetail.createdDate(UPDATED_CREATED_DATE).lastupdated(UPDATED_LASTUPDATED).arrivalDate(UPDATED_ARRIVAL_DATE);

        restTripdetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripdetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripdetail))
            )
            .andExpect(status().isOk());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
        Tripdetail testTripdetail = tripdetailList.get(tripdetailList.size() - 1);
        assertThat(testTripdetail.getMinimumList()).isEqualTo(DEFAULT_MINIMUM_LIST);
        assertThat(testTripdetail.getMaximumList()).isEqualTo(DEFAULT_MAXIMUM_LIST);
        assertThat(testTripdetail.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTripdetail.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testTripdetail.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testTripdetail.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
        assertThat(testTripdetail.getContentDate()).isEqualTo(DEFAULT_CONTENT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTripdetailWithPatch() throws Exception {
        // Initialize the database
        tripdetailRepository.saveAndFlush(tripdetail);

        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();

        // Update the tripdetail using partial update
        Tripdetail partialUpdatedTripdetail = new Tripdetail();
        partialUpdatedTripdetail.setId(tripdetail.getId());

        partialUpdatedTripdetail
            .minimumList(UPDATED_MINIMUM_LIST)
            .maximumList(UPDATED_MAXIMUM_LIST)
            .createdDate(UPDATED_CREATED_DATE)
            .lastupdated(UPDATED_LASTUPDATED)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .arrivalDate(UPDATED_ARRIVAL_DATE)
            .contentDate(UPDATED_CONTENT_DATE);

        restTripdetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripdetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripdetail))
            )
            .andExpect(status().isOk());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
        Tripdetail testTripdetail = tripdetailList.get(tripdetailList.size() - 1);
        assertThat(testTripdetail.getMinimumList()).isEqualTo(UPDATED_MINIMUM_LIST);
        assertThat(testTripdetail.getMaximumList()).isEqualTo(UPDATED_MAXIMUM_LIST);
        assertThat(testTripdetail.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTripdetail.getLastupdated()).isEqualTo(UPDATED_LASTUPDATED);
        assertThat(testTripdetail.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testTripdetail.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
        assertThat(testTripdetail.getContentDate()).isEqualTo(UPDATED_CONTENT_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTripdetail() throws Exception {
        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();
        tripdetail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripdetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripdetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripdetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripdetail() throws Exception {
        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();
        tripdetail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripdetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripdetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripdetail() throws Exception {
        int databaseSizeBeforeUpdate = tripdetailRepository.findAll().size();
        tripdetail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripdetailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripdetail))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tripdetail in the database
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripdetail() throws Exception {
        // Initialize the database
        tripdetailRepository.saveAndFlush(tripdetail);

        int databaseSizeBeforeDelete = tripdetailRepository.findAll().size();

        // Delete the tripdetail
        restTripdetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripdetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tripdetail> tripdetailList = tripdetailRepository.findAll();
        assertThat(tripdetailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
