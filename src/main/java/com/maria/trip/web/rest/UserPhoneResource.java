package com.maria.trip.web.rest;

import com.maria.trip.domain.UserPhone;
import com.maria.trip.repository.UserPhoneRepository;
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
 * REST controller for managing {@link com.maria.trip.domain.UserPhone}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserPhoneResource {

    private final Logger log = LoggerFactory.getLogger(UserPhoneResource.class);

    private static final String ENTITY_NAME = "userPhone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPhoneRepository userPhoneRepository;

    public UserPhoneResource(UserPhoneRepository userPhoneRepository) {
        this.userPhoneRepository = userPhoneRepository;
    }

    /**
     * {@code POST  /user-phones} : Create a new userPhone.
     *
     * @param userPhone the userPhone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPhone, or with status {@code 400 (Bad Request)} if the userPhone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-phones")
    public ResponseEntity<UserPhone> createUserPhone(@Valid @RequestBody UserPhone userPhone) throws URISyntaxException {
        log.debug("REST request to save UserPhone : {}", userPhone);
        if (userPhone.getId() != null) {
            throw new BadRequestAlertException("A new userPhone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPhone result = userPhoneRepository.save(userPhone);
        return ResponseEntity
            .created(new URI("/api/user-phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-phones/:id} : Updates an existing userPhone.
     *
     * @param id the id of the userPhone to save.
     * @param userPhone the userPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPhone,
     * or with status {@code 400 (Bad Request)} if the userPhone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-phones/{id}")
    public ResponseEntity<UserPhone> updateUserPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPhone userPhone
    ) throws URISyntaxException {
        log.debug("REST request to update UserPhone : {}, {}", id, userPhone);
        if (userPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserPhone result = userPhoneRepository.save(userPhone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPhone.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-phones/:id} : Partial updates given fields of an existing userPhone, field will ignore if it is null
     *
     * @param id the id of the userPhone to save.
     * @param userPhone the userPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPhone,
     * or with status {@code 400 (Bad Request)} if the userPhone is not valid,
     * or with status {@code 404 (Not Found)} if the userPhone is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-phones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPhone> partialUpdateUserPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPhone userPhone
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserPhone partially : {}, {}", id, userPhone);
        if (userPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPhone> result = userPhoneRepository
            .findById(userPhone.getId())
            .map(existingUserPhone -> {
                if (userPhone.getPhoneNumber() != null) {
                    existingUserPhone.setPhoneNumber(userPhone.getPhoneNumber());
                }
                if (userPhone.getVerified() != null) {
                    existingUserPhone.setVerified(userPhone.getVerified());
                }

                return existingUserPhone;
            })
            .map(userPhoneRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPhone.getId().toString())
        );
    }

    /**
     * {@code GET  /user-phones} : get all the userPhones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPhones in body.
     */
    @GetMapping("/user-phones")
    public List<UserPhone> getAllUserPhones() {
        log.debug("REST request to get all UserPhones");
        return userPhoneRepository.findAll();
    }

    /**
     * {@code GET  /user-phones/:id} : get the "id" userPhone.
     *
     * @param id the id of the userPhone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPhone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-phones/{id}")
    public ResponseEntity<UserPhone> getUserPhone(@PathVariable Long id) {
        log.debug("REST request to get UserPhone : {}", id);
        Optional<UserPhone> userPhone = userPhoneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userPhone);
    }

    /**
     * {@code DELETE  /user-phones/:id} : delete the "id" userPhone.
     *
     * @param id the id of the userPhone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-phones/{id}")
    public ResponseEntity<Void> deleteUserPhone(@PathVariable Long id) {
        log.debug("REST request to delete UserPhone : {}", id);
        userPhoneRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
