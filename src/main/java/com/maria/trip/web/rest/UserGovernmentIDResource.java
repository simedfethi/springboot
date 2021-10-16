package com.maria.trip.web.rest;

import com.maria.trip.domain.UserGovernmentID;
import com.maria.trip.repository.UserGovernmentIDRepository;
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
 * REST controller for managing {@link com.maria.trip.domain.UserGovernmentID}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserGovernmentIDResource {

    private final Logger log = LoggerFactory.getLogger(UserGovernmentIDResource.class);

    private static final String ENTITY_NAME = "userGovernmentID";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserGovernmentIDRepository userGovernmentIDRepository;

    public UserGovernmentIDResource(UserGovernmentIDRepository userGovernmentIDRepository) {
        this.userGovernmentIDRepository = userGovernmentIDRepository;
    }

    /**
     * {@code POST  /user-government-ids} : Create a new userGovernmentID.
     *
     * @param userGovernmentID the userGovernmentID to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userGovernmentID, or with status {@code 400 (Bad Request)} if the userGovernmentID has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-government-ids")
    public ResponseEntity<UserGovernmentID> createUserGovernmentID(@Valid @RequestBody UserGovernmentID userGovernmentID)
        throws URISyntaxException {
        log.debug("REST request to save UserGovernmentID : {}", userGovernmentID);
        if (userGovernmentID.getId() != null) {
            throw new BadRequestAlertException("A new userGovernmentID cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserGovernmentID result = userGovernmentIDRepository.save(userGovernmentID);
        return ResponseEntity
            .created(new URI("/api/user-government-ids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-government-ids/:id} : Updates an existing userGovernmentID.
     *
     * @param id the id of the userGovernmentID to save.
     * @param userGovernmentID the userGovernmentID to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGovernmentID,
     * or with status {@code 400 (Bad Request)} if the userGovernmentID is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userGovernmentID couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-government-ids/{id}")
    public ResponseEntity<UserGovernmentID> updateUserGovernmentID(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserGovernmentID userGovernmentID
    ) throws URISyntaxException {
        log.debug("REST request to update UserGovernmentID : {}, {}", id, userGovernmentID);
        if (userGovernmentID.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userGovernmentID.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userGovernmentIDRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserGovernmentID result = userGovernmentIDRepository.save(userGovernmentID);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userGovernmentID.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-government-ids/:id} : Partial updates given fields of an existing userGovernmentID, field will ignore if it is null
     *
     * @param id the id of the userGovernmentID to save.
     * @param userGovernmentID the userGovernmentID to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGovernmentID,
     * or with status {@code 400 (Bad Request)} if the userGovernmentID is not valid,
     * or with status {@code 404 (Not Found)} if the userGovernmentID is not found,
     * or with status {@code 500 (Internal Server Error)} if the userGovernmentID couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-government-ids/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserGovernmentID> partialUpdateUserGovernmentID(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserGovernmentID userGovernmentID
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserGovernmentID partially : {}, {}", id, userGovernmentID);
        if (userGovernmentID.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userGovernmentID.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userGovernmentIDRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserGovernmentID> result = userGovernmentIDRepository
            .findById(userGovernmentID.getId())
            .map(existingUserGovernmentID -> {
                if (userGovernmentID.getCountry() != null) {
                    existingUserGovernmentID.setCountry(userGovernmentID.getCountry());
                }
                if (userGovernmentID.getDocType() != null) {
                    existingUserGovernmentID.setDocType(userGovernmentID.getDocType());
                }
                if (userGovernmentID.getDocPicRect() != null) {
                    existingUserGovernmentID.setDocPicRect(userGovernmentID.getDocPicRect());
                }
                if (userGovernmentID.getDocPicRectContentType() != null) {
                    existingUserGovernmentID.setDocPicRectContentType(userGovernmentID.getDocPicRectContentType());
                }
                if (userGovernmentID.getDocPicVers() != null) {
                    existingUserGovernmentID.setDocPicVers(userGovernmentID.getDocPicVers());
                }
                if (userGovernmentID.getDocPicVersContentType() != null) {
                    existingUserGovernmentID.setDocPicVersContentType(userGovernmentID.getDocPicVersContentType());
                }
                if (userGovernmentID.getDocPicInst() != null) {
                    existingUserGovernmentID.setDocPicInst(userGovernmentID.getDocPicInst());
                }
                if (userGovernmentID.getDocPicInstContentType() != null) {
                    existingUserGovernmentID.setDocPicInstContentType(userGovernmentID.getDocPicInstContentType());
                }
                if (userGovernmentID.getIdVerified() != null) {
                    existingUserGovernmentID.setIdVerified(userGovernmentID.getIdVerified());
                }

                return existingUserGovernmentID;
            })
            .map(userGovernmentIDRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userGovernmentID.getId().toString())
        );
    }

    /**
     * {@code GET  /user-government-ids} : get all the userGovernmentIDS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userGovernmentIDS in body.
     */
    @GetMapping("/user-government-ids")
    public List<UserGovernmentID> getAllUserGovernmentIDS() {
        log.debug("REST request to get all UserGovernmentIDS");
        return userGovernmentIDRepository.findAll();
    }

    /**
     * {@code GET  /user-government-ids/:id} : get the "id" userGovernmentID.
     *
     * @param id the id of the userGovernmentID to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGovernmentID, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-government-ids/{id}")
    public ResponseEntity<UserGovernmentID> getUserGovernmentID(@PathVariable Long id) {
        log.debug("REST request to get UserGovernmentID : {}", id);
        Optional<UserGovernmentID> userGovernmentID = userGovernmentIDRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userGovernmentID);
    }

    /**
     * {@code DELETE  /user-government-ids/:id} : delete the "id" userGovernmentID.
     *
     * @param id the id of the userGovernmentID to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-government-ids/{id}")
    public ResponseEntity<Void> deleteUserGovernmentID(@PathVariable Long id) {
        log.debug("REST request to delete UserGovernmentID : {}", id);
        userGovernmentIDRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
