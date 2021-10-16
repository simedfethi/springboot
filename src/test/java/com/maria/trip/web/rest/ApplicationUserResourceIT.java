package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.ApplicationUser;
import com.maria.trip.repository.ApplicationUserRepository;
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
 * Integration tests for the {@link ApplicationUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/application-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserMockMvc;

    private ApplicationUser applicationUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .mobilePhone(DEFAULT_MOBILE_PHONE);
        return applicationUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUser createUpdatedEntity(EntityManager em) {
        ApplicationUser applicationUser = new ApplicationUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        return applicationUser;
    }

    @BeforeEach
    public void initTest() {
        applicationUser = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationUser() throws Exception {
        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();
        // Create the ApplicationUser
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicationUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApplicationUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testApplicationUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testApplicationUser.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void createApplicationUserWithExistingId() throws Exception {
        // Create the ApplicationUser with an existing ID
        applicationUser.setId(1L);

        int databaseSizeBeforeCreate = applicationUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setFirstName(null);

        // Create the ApplicationUser, which fails.

        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationUserRepository.findAll().size();
        // set the field null
        applicationUser.setLastName(null);

        // Create the ApplicationUser, which fails.

        restApplicationUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicationUsers() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get all the applicationUserList
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)));
    }

    @Test
    @Transactional
    void getApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        // Get the applicationUser
        restApplicationUserMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUser() throws Exception {
        // Get the applicationUser
        restApplicationUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser
        ApplicationUser updatedApplicationUser = applicationUserRepository.findById(applicationUser.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationUser are not directly saved in db
        em.detach(updatedApplicationUser);
        updatedApplicationUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicationUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicationUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testApplicationUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplicationUser.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser.lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicationUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicationUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testApplicationUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testApplicationUser.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserWithPatch() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();

        // Update the applicationUser using partial update
        ApplicationUser partialUpdatedApplicationUser = new ApplicationUser();
        partialUpdatedApplicationUser.setId(applicationUser.getId());

        partialUpdatedApplicationUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUser))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUser testApplicationUser = applicationUserList.get(applicationUserList.size() - 1);
        assertThat(testApplicationUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicationUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicationUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicationUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testApplicationUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApplicationUser.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUser() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserRepository.findAll().size();
        applicationUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUser in the database
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationUser() throws Exception {
        // Initialize the database
        applicationUserRepository.saveAndFlush(applicationUser);

        int databaseSizeBeforeDelete = applicationUserRepository.findAll().size();

        // Delete the applicationUser
        restApplicationUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();
        assertThat(applicationUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
