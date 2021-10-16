package com.maria.trip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maria.trip.IntegrationTest;
import com.maria.trip.domain.Notifications;
import com.maria.trip.repository.NotificationsRepository;
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
 * Integration tests for the {@link NotificationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationsMockMvc;

    private Notifications notifications;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notifications createEntity(EntityManager em) {
        Notifications notifications = new Notifications().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
        return notifications;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notifications createUpdatedEntity(EntityManager em) {
        Notifications notifications = new Notifications().title(UPDATED_TITLE).content(UPDATED_CONTENT);
        return notifications;
    }

    @BeforeEach
    public void initTest() {
        notifications = createEntity(em);
    }

    @Test
    @Transactional
    void createNotifications() throws Exception {
        int databaseSizeBeforeCreate = notificationsRepository.findAll().size();
        // Create the Notifications
        restNotificationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notifications)))
            .andExpect(status().isCreated());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeCreate + 1);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotifications.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createNotificationsWithExistingId() throws Exception {
        // Create the Notifications with an existing ID
        notifications.setId(1L);

        int databaseSizeBeforeCreate = notificationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notifications)))
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get all the notificationsList
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        // Get the notifications
        restNotificationsMockMvc
            .perform(get(ENTITY_API_URL_ID, notifications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notifications.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingNotifications() throws Exception {
        // Get the notifications
        restNotificationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications
        Notifications updatedNotifications = notificationsRepository.findById(notifications.getId()).get();
        // Disconnect from session so that the updates on updatedNotifications are not directly saved in db
        em.detach(updatedNotifications);
        updatedNotifications.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotifications.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNotifications))
            )
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotifications.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notifications.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notifications))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notifications))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notifications)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationsWithPatch() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications using partial update
        Notifications partialUpdatedNotifications = new Notifications();
        partialUpdatedNotifications.setId(notifications.getId());

        partialUpdatedNotifications.content(UPDATED_CONTENT);

        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotifications))
            )
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotifications.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateNotificationsWithPatch() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();

        // Update the notifications using partial update
        Notifications partialUpdatedNotifications = new Notifications();
        partialUpdatedNotifications.setId(notifications.getId());

        partialUpdatedNotifications.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotifications))
            )
            .andExpect(status().isOk());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
        Notifications testNotifications = notificationsList.get(notificationsList.size() - 1);
        assertThat(testNotifications.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotifications.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notifications.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notifications))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notifications))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotifications() throws Exception {
        int databaseSizeBeforeUpdate = notificationsRepository.findAll().size();
        notifications.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(notifications))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notifications in the database
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotifications() throws Exception {
        // Initialize the database
        notificationsRepository.saveAndFlush(notifications);

        int databaseSizeBeforeDelete = notificationsRepository.findAll().size();

        // Delete the notifications
        restNotificationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, notifications.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notifications> notificationsList = notificationsRepository.findAll();
        assertThat(notificationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
