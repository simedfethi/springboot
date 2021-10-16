package com.maria.trip.web.rest;

import com.maria.trip.domain.Notifications;
import com.maria.trip.repository.NotificationsRepository;
import com.maria.trip.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.maria.trip.domain.Notifications}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NotificationsResource {

    private final Logger log = LoggerFactory.getLogger(NotificationsResource.class);

    private static final String ENTITY_NAME = "notifications";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationsRepository notificationsRepository;

    public NotificationsResource(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    /**
     * {@code POST  /notifications} : Create a new notifications.
     *
     * @param notifications the notifications to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notifications, or with status {@code 400 (Bad Request)} if the notifications has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notifications")
    public ResponseEntity<Notifications> createNotifications(@Valid @RequestBody Notifications notifications) throws URISyntaxException {
        log.debug("REST request to save Notifications : {}", notifications);
        if (notifications.getId() != null) {
            throw new BadRequestAlertException("A new notifications cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notifications result = notificationsRepository.save(notifications);
        return ResponseEntity
            .created(new URI("/api/notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notifications/:id} : Updates an existing notifications.
     *
     * @param id the id of the notifications to save.
     * @param notifications the notifications to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notifications,
     * or with status {@code 400 (Bad Request)} if the notifications is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notifications couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notifications/{id}")
    public ResponseEntity<Notifications> updateNotifications(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Notifications notifications
    ) throws URISyntaxException {
        log.debug("REST request to update Notifications : {}, {}", id, notifications);
        if (notifications.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notifications.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Notifications result = notificationsRepository.save(notifications);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notifications.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notifications/:id} : Partial updates given fields of an existing notifications, field will ignore if it is null
     *
     * @param id the id of the notifications to save.
     * @param notifications the notifications to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notifications,
     * or with status {@code 400 (Bad Request)} if the notifications is not valid,
     * or with status {@code 404 (Not Found)} if the notifications is not found,
     * or with status {@code 500 (Internal Server Error)} if the notifications couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Notifications> partialUpdateNotifications(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Notifications notifications
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notifications partially : {}, {}", id, notifications);
        if (notifications.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notifications.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Notifications> result = notificationsRepository
            .findById(notifications.getId())
            .map(existingNotifications -> {
                if (notifications.getTitle() != null) {
                    existingNotifications.setTitle(notifications.getTitle());
                }
                if (notifications.getContent() != null) {
                    existingNotifications.setContent(notifications.getContent());
                }

                return existingNotifications;
            })
            .map(notificationsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notifications.getId().toString())
        );
    }

    /**
     * {@code GET  /notifications} : get all the notifications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/notifications")
    public List<Notifications> getAllNotifications() {
        log.debug("REST request to get all Notifications");
        return notificationsRepository.findAll();
    }

    /**
     * {@code GET  /notifications/:id} : get the "id" notifications.
     *
     * @param id the id of the notifications to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notifications, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notifications/{id}")
    public ResponseEntity<Notifications> getNotifications(@PathVariable Long id) {
        log.debug("REST request to get Notifications : {}", id);
        Optional<Notifications> notifications = notificationsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notifications);
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notifications.
     *
     * @param id the id of the notifications to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotifications(@PathVariable Long id) {
        log.debug("REST request to delete Notifications : {}", id);
        notificationsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
