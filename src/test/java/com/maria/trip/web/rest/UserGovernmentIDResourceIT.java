package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.UserGovernmentID;
import com.maria.trip.domain.enumeration.GovernmentIDType;
import com.maria.trip.repository.UserGovernmentIDRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link UserGovernmentIDResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserGovernmentIDResourceIT {

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final GovernmentIDType DEFAULT_DOC_TYPE = GovernmentIDType.ID;
    private static final GovernmentIDType UPDATED_DOC_TYPE = GovernmentIDType.PASSPORT;

    private static final byte[] DEFAULT_DOC_PIC_RECT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOC_PIC_RECT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOC_PIC_RECT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOC_PIC_RECT_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_DOC_PIC_VERS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOC_PIC_VERS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOC_PIC_VERS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOC_PIC_VERS_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_DOC_PIC_INST = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOC_PIC_INST = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOC_PIC_INST_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOC_PIC_INST_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ID_VERIFIED = false;
    private static final Boolean UPDATED_ID_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/user-government-ids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserGovernmentIDRepository userGovernmentIDRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserGovernmentIDMockMvc;

    private UserGovernmentID userGovernmentID;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGovernmentID createEntity(EntityManager em) {
        UserGovernmentID userGovernmentID = new UserGovernmentID()
            .country(DEFAULT_COUNTRY)
            .docType(DEFAULT_DOC_TYPE)
            .docPicRect(DEFAULT_DOC_PIC_RECT)
            .docPicRectContentType(DEFAULT_DOC_PIC_RECT_CONTENT_TYPE)
            .docPicVers(DEFAULT_DOC_PIC_VERS)
            .docPicVersContentType(DEFAULT_DOC_PIC_VERS_CONTENT_TYPE)
            .docPicInst(DEFAULT_DOC_PIC_INST)
            .docPicInstContentType(DEFAULT_DOC_PIC_INST_CONTENT_TYPE)
            .idVerified(DEFAULT_ID_VERIFIED);
        return userGovernmentID;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserGovernmentID createUpdatedEntity(EntityManager em) {
        UserGovernmentID userGovernmentID = new UserGovernmentID()
            .country(UPDATED_COUNTRY)
            .docType(UPDATED_DOC_TYPE)
            .docPicRect(UPDATED_DOC_PIC_RECT)
            .docPicRectContentType(UPDATED_DOC_PIC_RECT_CONTENT_TYPE)
            .docPicVers(UPDATED_DOC_PIC_VERS)
            .docPicVersContentType(UPDATED_DOC_PIC_VERS_CONTENT_TYPE)
            .docPicInst(UPDATED_DOC_PIC_INST)
            .docPicInstContentType(UPDATED_DOC_PIC_INST_CONTENT_TYPE)
            .idVerified(UPDATED_ID_VERIFIED);
        return userGovernmentID;
    }

    @BeforeEach
    public void initTest() {
        userGovernmentID = createEntity(em);
    }

    @Test
    @Transactional
    void createUserGovernmentID() throws Exception {
        int databaseSizeBeforeCreate = userGovernmentIDRepository.findAll().size();
        // Create the UserGovernmentID
        restUserGovernmentIDMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isCreated());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeCreate + 1);
        UserGovernmentID testUserGovernmentID = userGovernmentIDList.get(userGovernmentIDList.size() - 1);
        assertThat(testUserGovernmentID.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserGovernmentID.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testUserGovernmentID.getDocPicRect()).isEqualTo(DEFAULT_DOC_PIC_RECT);
        assertThat(testUserGovernmentID.getDocPicRectContentType()).isEqualTo(DEFAULT_DOC_PIC_RECT_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicVers()).isEqualTo(DEFAULT_DOC_PIC_VERS);
        assertThat(testUserGovernmentID.getDocPicVersContentType()).isEqualTo(DEFAULT_DOC_PIC_VERS_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicInst()).isEqualTo(DEFAULT_DOC_PIC_INST);
        assertThat(testUserGovernmentID.getDocPicInstContentType()).isEqualTo(DEFAULT_DOC_PIC_INST_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getIdVerified()).isEqualTo(DEFAULT_ID_VERIFIED);
    }

    @Test
    @Transactional
    void createUserGovernmentIDWithExistingId() throws Exception {
        // Create the UserGovernmentID with an existing ID
        userGovernmentID.setId(1L);

        int databaseSizeBeforeCreate = userGovernmentIDRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserGovernmentIDMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserGovernmentIDS() throws Exception {
        // Initialize the database
        userGovernmentIDRepository.saveAndFlush(userGovernmentID);

        // Get all the userGovernmentIDList
        restUserGovernmentIDMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userGovernmentID.getId().intValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].docPicRectContentType").value(hasItem(DEFAULT_DOC_PIC_RECT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docPicRect").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC_PIC_RECT))))
            .andExpect(jsonPath("$.[*].docPicVersContentType").value(hasItem(DEFAULT_DOC_PIC_VERS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docPicVers").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC_PIC_VERS))))
            .andExpect(jsonPath("$.[*].docPicInstContentType").value(hasItem(DEFAULT_DOC_PIC_INST_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docPicInst").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOC_PIC_INST))))
            .andExpect(jsonPath("$.[*].idVerified").value(hasItem(DEFAULT_ID_VERIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getUserGovernmentID() throws Exception {
        // Initialize the database
        userGovernmentIDRepository.saveAndFlush(userGovernmentID);

        // Get the userGovernmentID
        restUserGovernmentIDMockMvc
            .perform(get(ENTITY_API_URL_ID, userGovernmentID.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userGovernmentID.getId().intValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.docPicRectContentType").value(DEFAULT_DOC_PIC_RECT_CONTENT_TYPE))
            .andExpect(jsonPath("$.docPicRect").value(Base64Utils.encodeToString(DEFAULT_DOC_PIC_RECT)))
            .andExpect(jsonPath("$.docPicVersContentType").value(DEFAULT_DOC_PIC_VERS_CONTENT_TYPE))
            .andExpect(jsonPath("$.docPicVers").value(Base64Utils.encodeToString(DEFAULT_DOC_PIC_VERS)))
            .andExpect(jsonPath("$.docPicInstContentType").value(DEFAULT_DOC_PIC_INST_CONTENT_TYPE))
            .andExpect(jsonPath("$.docPicInst").value(Base64Utils.encodeToString(DEFAULT_DOC_PIC_INST)))
            .andExpect(jsonPath("$.idVerified").value(DEFAULT_ID_VERIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserGovernmentID() throws Exception {
        // Get the userGovernmentID
        restUserGovernmentIDMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserGovernmentID() throws Exception {
        // Initialize the database
        userGovernmentIDRepository.saveAndFlush(userGovernmentID);

        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();

        // Update the userGovernmentID
        UserGovernmentID updatedUserGovernmentID = userGovernmentIDRepository.findById(userGovernmentID.getId()).get();
        // Disconnect from session so that the updates on updatedUserGovernmentID are not directly saved in db
        em.detach(updatedUserGovernmentID);
        updatedUserGovernmentID
            .country(UPDATED_COUNTRY)
            .docType(UPDATED_DOC_TYPE)
            .docPicRect(UPDATED_DOC_PIC_RECT)
            .docPicRectContentType(UPDATED_DOC_PIC_RECT_CONTENT_TYPE)
            .docPicVers(UPDATED_DOC_PIC_VERS)
            .docPicVersContentType(UPDATED_DOC_PIC_VERS_CONTENT_TYPE)
            .docPicInst(UPDATED_DOC_PIC_INST)
            .docPicInstContentType(UPDATED_DOC_PIC_INST_CONTENT_TYPE)
            .idVerified(UPDATED_ID_VERIFIED);

        restUserGovernmentIDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserGovernmentID.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserGovernmentID))
            )
            .andExpect(status().isOk());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
        UserGovernmentID testUserGovernmentID = userGovernmentIDList.get(userGovernmentIDList.size() - 1);
        assertThat(testUserGovernmentID.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserGovernmentID.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testUserGovernmentID.getDocPicRect()).isEqualTo(UPDATED_DOC_PIC_RECT);
        assertThat(testUserGovernmentID.getDocPicRectContentType()).isEqualTo(UPDATED_DOC_PIC_RECT_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicVers()).isEqualTo(UPDATED_DOC_PIC_VERS);
        assertThat(testUserGovernmentID.getDocPicVersContentType()).isEqualTo(UPDATED_DOC_PIC_VERS_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicInst()).isEqualTo(UPDATED_DOC_PIC_INST);
        assertThat(testUserGovernmentID.getDocPicInstContentType()).isEqualTo(UPDATED_DOC_PIC_INST_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getIdVerified()).isEqualTo(UPDATED_ID_VERIFIED);
    }

    @Test
    @Transactional
    void putNonExistingUserGovernmentID() throws Exception {
        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();
        userGovernmentID.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGovernmentIDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userGovernmentID.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserGovernmentID() throws Exception {
        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();
        userGovernmentID.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGovernmentIDMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserGovernmentID() throws Exception {
        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();
        userGovernmentID.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGovernmentIDMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserGovernmentIDWithPatch() throws Exception {
        // Initialize the database
        userGovernmentIDRepository.saveAndFlush(userGovernmentID);

        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();

        // Update the userGovernmentID using partial update
        UserGovernmentID partialUpdatedUserGovernmentID = new UserGovernmentID();
        partialUpdatedUserGovernmentID.setId(userGovernmentID.getId());

        partialUpdatedUserGovernmentID
            .country(UPDATED_COUNTRY)
            .docPicInst(UPDATED_DOC_PIC_INST)
            .docPicInstContentType(UPDATED_DOC_PIC_INST_CONTENT_TYPE);

        restUserGovernmentIDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserGovernmentID.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserGovernmentID))
            )
            .andExpect(status().isOk());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
        UserGovernmentID testUserGovernmentID = userGovernmentIDList.get(userGovernmentIDList.size() - 1);
        assertThat(testUserGovernmentID.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserGovernmentID.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testUserGovernmentID.getDocPicRect()).isEqualTo(DEFAULT_DOC_PIC_RECT);
        assertThat(testUserGovernmentID.getDocPicRectContentType()).isEqualTo(DEFAULT_DOC_PIC_RECT_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicVers()).isEqualTo(DEFAULT_DOC_PIC_VERS);
        assertThat(testUserGovernmentID.getDocPicVersContentType()).isEqualTo(DEFAULT_DOC_PIC_VERS_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicInst()).isEqualTo(UPDATED_DOC_PIC_INST);
        assertThat(testUserGovernmentID.getDocPicInstContentType()).isEqualTo(UPDATED_DOC_PIC_INST_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getIdVerified()).isEqualTo(DEFAULT_ID_VERIFIED);
    }

    @Test
    @Transactional
    void fullUpdateUserGovernmentIDWithPatch() throws Exception {
        // Initialize the database
        userGovernmentIDRepository.saveAndFlush(userGovernmentID);

        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();

        // Update the userGovernmentID using partial update
        UserGovernmentID partialUpdatedUserGovernmentID = new UserGovernmentID();
        partialUpdatedUserGovernmentID.setId(userGovernmentID.getId());

        partialUpdatedUserGovernmentID
            .country(UPDATED_COUNTRY)
            .docType(UPDATED_DOC_TYPE)
            .docPicRect(UPDATED_DOC_PIC_RECT)
            .docPicRectContentType(UPDATED_DOC_PIC_RECT_CONTENT_TYPE)
            .docPicVers(UPDATED_DOC_PIC_VERS)
            .docPicVersContentType(UPDATED_DOC_PIC_VERS_CONTENT_TYPE)
            .docPicInst(UPDATED_DOC_PIC_INST)
            .docPicInstContentType(UPDATED_DOC_PIC_INST_CONTENT_TYPE)
            .idVerified(UPDATED_ID_VERIFIED);

        restUserGovernmentIDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserGovernmentID.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserGovernmentID))
            )
            .andExpect(status().isOk());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
        UserGovernmentID testUserGovernmentID = userGovernmentIDList.get(userGovernmentIDList.size() - 1);
        assertThat(testUserGovernmentID.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserGovernmentID.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testUserGovernmentID.getDocPicRect()).isEqualTo(UPDATED_DOC_PIC_RECT);
        assertThat(testUserGovernmentID.getDocPicRectContentType()).isEqualTo(UPDATED_DOC_PIC_RECT_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicVers()).isEqualTo(UPDATED_DOC_PIC_VERS);
        assertThat(testUserGovernmentID.getDocPicVersContentType()).isEqualTo(UPDATED_DOC_PIC_VERS_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getDocPicInst()).isEqualTo(UPDATED_DOC_PIC_INST);
        assertThat(testUserGovernmentID.getDocPicInstContentType()).isEqualTo(UPDATED_DOC_PIC_INST_CONTENT_TYPE);
        assertThat(testUserGovernmentID.getIdVerified()).isEqualTo(UPDATED_ID_VERIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingUserGovernmentID() throws Exception {
        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();
        userGovernmentID.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserGovernmentIDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userGovernmentID.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserGovernmentID() throws Exception {
        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();
        userGovernmentID.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGovernmentIDMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserGovernmentID() throws Exception {
        int databaseSizeBeforeUpdate = userGovernmentIDRepository.findAll().size();
        userGovernmentID.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserGovernmentIDMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userGovernmentID))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserGovernmentID in the database
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserGovernmentID() throws Exception {
        // Initialize the database
        userGovernmentIDRepository.saveAndFlush(userGovernmentID);

        int databaseSizeBeforeDelete = userGovernmentIDRepository.findAll().size();

        // Delete the userGovernmentID
        restUserGovernmentIDMockMvc
            .perform(delete(ENTITY_API_URL_ID, userGovernmentID.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserGovernmentID> userGovernmentIDList = userGovernmentIDRepository.findAll();
        assertThat(userGovernmentIDList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
