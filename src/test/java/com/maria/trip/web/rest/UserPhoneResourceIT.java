package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.UserPhone;
import com.maria.trip.repository.UserPhoneRepository;
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
 * Integration tests for the {@link UserPhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserPhoneResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/user-phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserPhoneRepository userPhoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPhoneMockMvc;

    private UserPhone userPhone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPhone createEntity(EntityManager em) {
        UserPhone userPhone = new UserPhone().phoneNumber(DEFAULT_PHONE_NUMBER).verified(DEFAULT_VERIFIED);
        return userPhone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPhone createUpdatedEntity(EntityManager em) {
        UserPhone userPhone = new UserPhone().phoneNumber(UPDATED_PHONE_NUMBER).verified(UPDATED_VERIFIED);
        return userPhone;
    }

    @BeforeEach
    public void initTest() {
        userPhone = createEntity(em);
    }

    @Test
    @Transactional
    void createUserPhone() throws Exception {
        int databaseSizeBeforeCreate = userPhoneRepository.findAll().size();
        // Create the UserPhone
        restUserPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPhone)))
            .andExpect(status().isCreated());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeCreate + 1);
        UserPhone testUserPhone = userPhoneList.get(userPhoneList.size() - 1);
        assertThat(testUserPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUserPhone.getVerified()).isEqualTo(DEFAULT_VERIFIED);
    }

    @Test
    @Transactional
    void createUserPhoneWithExistingId() throws Exception {
        // Create the UserPhone with an existing ID
        userPhone.setId(1L);

        int databaseSizeBeforeCreate = userPhoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPhone)))
            .andExpect(status().isBadRequest());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPhoneRepository.findAll().size();
        // set the field null
        userPhone.setPhoneNumber(null);

        // Create the UserPhone, which fails.

        restUserPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPhone)))
            .andExpect(status().isBadRequest());

        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserPhones() throws Exception {
        // Initialize the database
        userPhoneRepository.saveAndFlush(userPhone);

        // Get all the userPhoneList
        restUserPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getUserPhone() throws Exception {
        // Initialize the database
        userPhoneRepository.saveAndFlush(userPhone);

        // Get the userPhone
        restUserPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, userPhone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPhone.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserPhone() throws Exception {
        // Get the userPhone
        restUserPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserPhone() throws Exception {
        // Initialize the database
        userPhoneRepository.saveAndFlush(userPhone);

        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();

        // Update the userPhone
        UserPhone updatedUserPhone = userPhoneRepository.findById(userPhone.getId()).get();
        // Disconnect from session so that the updates on updatedUserPhone are not directly saved in db
        em.detach(updatedUserPhone);
        updatedUserPhone.phoneNumber(UPDATED_PHONE_NUMBER).verified(UPDATED_VERIFIED);

        restUserPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserPhone))
            )
            .andExpect(status().isOk());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
        UserPhone testUserPhone = userPhoneList.get(userPhoneList.size() - 1);
        assertThat(testUserPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserPhone.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void putNonExistingUserPhone() throws Exception {
        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();
        userPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPhone() throws Exception {
        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();
        userPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPhone() throws Exception {
        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();
        userPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPhoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userPhone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPhoneWithPatch() throws Exception {
        // Initialize the database
        userPhoneRepository.saveAndFlush(userPhone);

        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();

        // Update the userPhone using partial update
        UserPhone partialUpdatedUserPhone = new UserPhone();
        partialUpdatedUserPhone.setId(userPhone.getId());

        partialUpdatedUserPhone.phoneNumber(UPDATED_PHONE_NUMBER).verified(UPDATED_VERIFIED);

        restUserPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPhone))
            )
            .andExpect(status().isOk());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
        UserPhone testUserPhone = userPhoneList.get(userPhoneList.size() - 1);
        assertThat(testUserPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserPhone.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void fullUpdateUserPhoneWithPatch() throws Exception {
        // Initialize the database
        userPhoneRepository.saveAndFlush(userPhone);

        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();

        // Update the userPhone using partial update
        UserPhone partialUpdatedUserPhone = new UserPhone();
        partialUpdatedUserPhone.setId(userPhone.getId());

        partialUpdatedUserPhone.phoneNumber(UPDATED_PHONE_NUMBER).verified(UPDATED_VERIFIED);

        restUserPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPhone))
            )
            .andExpect(status().isOk());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
        UserPhone testUserPhone = userPhoneList.get(userPhoneList.size() - 1);
        assertThat(testUserPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserPhone.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingUserPhone() throws Exception {
        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();
        userPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPhone() throws Exception {
        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();
        userPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPhone() throws Exception {
        int databaseSizeBeforeUpdate = userPhoneRepository.findAll().size();
        userPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userPhone))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPhone in the database
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPhone() throws Exception {
        // Initialize the database
        userPhoneRepository.saveAndFlush(userPhone);

        int databaseSizeBeforeDelete = userPhoneRepository.findAll().size();

        // Delete the userPhone
        restUserPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPhone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPhone> userPhoneList = userPhoneRepository.findAll();
        assertThat(userPhoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
