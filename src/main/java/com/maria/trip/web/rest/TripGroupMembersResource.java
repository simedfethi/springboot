package com.maria.trip.web.rest;

import com.maria.trip.domain.TripGroupMembers;
import com.maria.trip.repository.TripGroupMembersRepository;
import com.maria.trip.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.maria.trip.domain.TripGroupMembers}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripGroupMembersResource {

    private final Logger log = LoggerFactory.getLogger(TripGroupMembersResource.class);

    private static final String ENTITY_NAME = "tripGroupMembers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripGroupMembersRepository tripGroupMembersRepository;

    public TripGroupMembersResource(TripGroupMembersRepository tripGroupMembersRepository) {
        this.tripGroupMembersRepository = tripGroupMembersRepository;
    }

    /**
     * {@code POST  /trip-group-members} : Create a new tripGroupMembers.
     *
     * @param tripGroupMembers the tripGroupMembers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripGroupMembers, or with status {@code 400 (Bad Request)} if the tripGroupMembers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trip-group-members")
    public ResponseEntity<TripGroupMembers> createTripGroupMembers(@RequestBody TripGroupMembers tripGroupMembers)
        throws URISyntaxException {
        log.debug("REST request to save TripGroupMembers : {}", tripGroupMembers);
        if (tripGroupMembers.getId() != null) {
            throw new BadRequestAlertException("A new tripGroupMembers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripGroupMembers result = tripGroupMembersRepository.save(tripGroupMembers);
        return ResponseEntity
            .created(new URI("/api/trip-group-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trip-group-members/:id} : Updates an existing tripGroupMembers.
     *
     * @param id the id of the tripGroupMembers to save.
     * @param tripGroupMembers the tripGroupMembers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripGroupMembers,
     * or with status {@code 400 (Bad Request)} if the tripGroupMembers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripGroupMembers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trip-group-members/{id}")
    public ResponseEntity<TripGroupMembers> updateTripGroupMembers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripGroupMembers tripGroupMembers
    ) throws URISyntaxException {
        log.debug("REST request to update TripGroupMembers : {}, {}", id, tripGroupMembers);
        if (tripGroupMembers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripGroupMembers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripGroupMembersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripGroupMembers result = tripGroupMembersRepository.save(tripGroupMembers);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripGroupMembers.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trip-group-members/:id} : Partial updates given fields of an existing tripGroupMembers, field will ignore if it is null
     *
     * @param id the id of the tripGroupMembers to save.
     * @param tripGroupMembers the tripGroupMembers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripGroupMembers,
     * or with status {@code 400 (Bad Request)} if the tripGroupMembers is not valid,
     * or with status {@code 404 (Not Found)} if the tripGroupMembers is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripGroupMembers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trip-group-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripGroupMembers> partialUpdateTripGroupMembers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripGroupMembers tripGroupMembers
    ) throws URISyntaxException {
        log.debug("REST request to partial update TripGroupMembers partially : {}, {}", id, tripGroupMembers);
        if (tripGroupMembers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripGroupMembers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripGroupMembersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripGroupMembers> result = tripGroupMembersRepository
            .findById(tripGroupMembers.getId())
            .map(existingTripGroupMembers -> {
                return existingTripGroupMembers;
            })
            .map(tripGroupMembersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripGroupMembers.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-group-members} : get all the tripGroupMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripGroupMembers in body.
     */
    @GetMapping("/trip-group-members")
    public List<TripGroupMembers> getAllTripGroupMembers() {
        log.debug("REST request to get all TripGroupMembers");
        return tripGroupMembersRepository.findAll();
    }

    /**
     * {@code GET  /trip-group-members/:id} : get the "id" tripGroupMembers.
     *
     * @param id the id of the tripGroupMembers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripGroupMembers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trip-group-members/{id}")
    public ResponseEntity<TripGroupMembers> getTripGroupMembers(@PathVariable Long id) {
        log.debug("REST request to get TripGroupMembers : {}", id);
        Optional<TripGroupMembers> tripGroupMembers = tripGroupMembersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tripGroupMembers);
    }

    /**
     * {@code DELETE  /trip-group-members/:id} : delete the "id" tripGroupMembers.
     *
     * @param id the id of the tripGroupMembers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trip-group-members/{id}")
    public ResponseEntity<Void> deleteTripGroupMembers(@PathVariable Long id) {
        log.debug("REST request to delete TripGroupMembers : {}", id);
        tripGroupMembersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
