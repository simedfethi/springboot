package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.AdressList;
import com.maria.trip.repository.AdressListRepository;
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
 * Integration tests for the {@link AdressListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdressListResourceIT {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_SUITE = "AAAAAAAAAA";
    private static final String UPDATED_STREET_SUITE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String ENTITY_API_URL = "/api/adress-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdressListRepository adressListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdressListMockMvc;

    private AdressList adressList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdressList createEntity(EntityManager em) {
        AdressList adressList = new AdressList()
            .street(DEFAULT_STREET)
            .streetSuite(DEFAULT_STREET_SUITE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return adressList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdressList createUpdatedEntity(EntityManager em) {
        AdressList adressList = new AdressList()
            .street(UPDATED_STREET)
            .streetSuite(UPDATED_STREET_SUITE)
            .postalCode(UPDATED_POSTAL_CODE)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        return adressList;
    }

    @BeforeEach
    public void initTest() {
        adressList = createEntity(em);
    }

    @Test
    @Transactional
    void createAdressList() throws Exception {
        int databaseSizeBeforeCreate = adressListRepository.findAll().size();
        // Create the AdressList
        restAdressListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adressList)))
            .andExpect(status().isCreated());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeCreate + 1);
        AdressList testAdressList = adressListList.get(adressListList.size() - 1);
        assertThat(testAdressList.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testAdressList.getStreetSuite()).isEqualTo(DEFAULT_STREET_SUITE);
        assertThat(testAdressList.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAdressList.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAdressList.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAdressList.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testAdressList.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createAdressListWithExistingId() throws Exception {
        // Create the AdressList with an existing ID
        adressList.setId(1L);

        int databaseSizeBeforeCreate = adressListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdressListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adressList)))
            .andExpect(status().isBadRequest());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdressLists() throws Exception {
        // Initialize the database
        adressListRepository.saveAndFlush(adressList);

        // Get all the adressListList
        restAdressListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adressList.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].streetSuite").value(hasItem(DEFAULT_STREET_SUITE)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    void getAdressList() throws Exception {
        // Initialize the database
        adressListRepository.saveAndFlush(adressList);

        // Get the adressList
        restAdressListMockMvc
            .perform(get(ENTITY_API_URL_ID, adressList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adressList.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.streetSuite").value(DEFAULT_STREET_SUITE))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAdressList() throws Exception {
        // Get the adressList
        restAdressListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdressList() throws Exception {
        // Initialize the database
        adressListRepository.saveAndFlush(adressList);

        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();

        // Update the adressList
        AdressList updatedAdressList = adressListRepository.findById(adressList.getId()).get();
        // Disconnect from session so that the updates on updatedAdressList are not directly saved in db
        em.detach(updatedAdressList);
        updatedAdressList
            .street(UPDATED_STREET)
            .streetSuite(UPDATED_STREET_SUITE)
            .postalCode(UPDATED_POSTAL_CODE)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restAdressListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdressList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdressList))
            )
            .andExpect(status().isOk());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
        AdressList testAdressList = adressListList.get(adressListList.size() - 1);
        assertThat(testAdressList.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAdressList.getStreetSuite()).isEqualTo(UPDATED_STREET_SUITE);
        assertThat(testAdressList.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAdressList.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAdressList.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAdressList.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAdressList.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingAdressList() throws Exception {
        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();
        adressList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdressListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adressList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adressList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdressList() throws Exception {
        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();
        adressList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdressListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adressList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdressList() throws Exception {
        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();
        adressList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdressListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adressList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdressListWithPatch() throws Exception {
        // Initialize the database
        adressListRepository.saveAndFlush(adressList);

        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();

        // Update the adressList using partial update
        AdressList partialUpdatedAdressList = new AdressList();
        partialUpdatedAdressList.setId(adressList.getId());

        partialUpdatedAdressList
            .street(UPDATED_STREET)
            .streetSuite(UPDATED_STREET_SUITE)
            .postalCode(UPDATED_POSTAL_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restAdressListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdressList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdressList))
            )
            .andExpect(status().isOk());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
        AdressList testAdressList = adressListList.get(adressListList.size() - 1);
        assertThat(testAdressList.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAdressList.getStreetSuite()).isEqualTo(UPDATED_STREET_SUITE);
        assertThat(testAdressList.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAdressList.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAdressList.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAdressList.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAdressList.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateAdressListWithPatch() throws Exception {
        // Initialize the database
        adressListRepository.saveAndFlush(adressList);

        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();

        // Update the adressList using partial update
        AdressList partialUpdatedAdressList = new AdressList();
        partialUpdatedAdressList.setId(adressList.getId());

        partialUpdatedAdressList
            .street(UPDATED_STREET)
            .streetSuite(UPDATED_STREET_SUITE)
            .postalCode(UPDATED_POSTAL_CODE)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restAdressListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdressList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdressList))
            )
            .andExpect(status().isOk());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
        AdressList testAdressList = adressListList.get(adressListList.size() - 1);
        assertThat(testAdressList.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAdressList.getStreetSuite()).isEqualTo(UPDATED_STREET_SUITE);
        assertThat(testAdressList.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAdressList.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAdressList.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAdressList.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAdressList.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingAdressList() throws Exception {
        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();
        adressList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdressListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adressList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adressList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdressList() throws Exception {
        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();
        adressList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdressListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adressList))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdressList() throws Exception {
        int databaseSizeBeforeUpdate = adressListRepository.findAll().size();
        adressList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdressListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adressList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdressList in the database
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdressList() throws Exception {
        // Initialize the database
        adressListRepository.saveAndFlush(adressList);

        int databaseSizeBeforeDelete = adressListRepository.findAll().size();

        // Delete the adressList
        restAdressListMockMvc
            .perform(delete(ENTITY_API_URL_ID, adressList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdressList> adressListList = adressListRepository.findAll();
        assertThat(adressListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
