package com.maria.trip.web.rest;

import com.maria.trip.domain.TripGroups;
import com.maria.trip.repository.TripGroupsRepository;
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
 * REST controller for managing {@link com.maria.trip.domain.TripGroups}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripGroupsResource {

    private final Logger log = LoggerFactory.getLogger(TripGroupsResource.class);

    private static final String ENTITY_NAME = "tripGroups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripGroupsRepository tripGroupsRepository;

    public TripGroupsResource(TripGroupsRepository tripGroupsRepository) {
        this.tripGroupsRepository = tripGroupsRepository;
    }

    /**
     * {@code POST  /trip-groups} : Create a new tripGroups.
     *
     * @param tripGroups the tripGroups to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripGroups, or with status {@code 400 (Bad Request)} if the tripGroups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trip-groups")
    public ResponseEntity<TripGroups> createTripGroups(@Valid @RequestBody TripGroups tripGroups) throws URISyntaxException {
        log.debug("REST request to save TripGroups : {}", tripGroups);
        if (tripGroups.getId() != null) {
            throw new BadRequestAlertException("A new tripGroups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripGroups result = tripGroupsRepository.save(tripGroups);
        return ResponseEntity
            .created(new URI("/api/trip-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trip-groups/:id} : Updates an existing tripGroups.
     *
     * @param id the id of the tripGroups to save.
     * @param tripGroups the tripGroups to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripGroups,
     * or with status {@code 400 (Bad Request)} if the tripGroups is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripGroups couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trip-groups/{id}")
    public ResponseEntity<TripGroups> updateTripGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TripGroups tripGroups
    ) throws URISyntaxException {
        log.debug("REST request to update TripGroups : {}, {}", id, tripGroups);
        if (tripGroups.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripGroups.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripGroups result = tripGroupsRepository.save(tripGroups);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripGroups.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trip-groups/:id} : Partial updates given fields of an existing tripGroups, field will ignore if it is null
     *
     * @param id the id of the tripGroups to save.
     * @param tripGroups the tripGroups to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripGroups,
     * or with status {@code 400 (Bad Request)} if the tripGroups is not valid,
     * or with status {@code 404 (Not Found)} if the tripGroups is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripGroups couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trip-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripGroups> partialUpdateTripGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TripGroups tripGroups
    ) throws URISyntaxException {
        log.debug("REST request to partial update TripGroups partially : {}, {}", id, tripGroups);
        if (tripGroups.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripGroups.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripGroups> result = tripGroupsRepository
            .findById(tripGroups.getId())
            .map(existingTripGroups -> {
                if (tripGroups.getTitle() != null) {
                    existingTripGroups.setTitle(tripGroups.getTitle());
                }
                if (tripGroups.getDescription() != null) {
                    existingTripGroups.setDescription(tripGroups.getDescription());
                }

                return existingTripGroups;
            })
            .map(tripGroupsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripGroups.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-groups} : get all the tripGroups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripGroups in body.
     */
    @GetMapping("/trip-groups")
    public List<TripGroups> getAllTripGroups(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all TripGroups");
        return tripGroupsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /trip-groups/:id} : get the "id" tripGroups.
     *
     * @param id the id of the tripGroups to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripGroups, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trip-groups/{id}")
    public ResponseEntity<TripGroups> getTripGroups(@PathVariable Long id) {
        log.debug("REST request to get TripGroups : {}", id);
        Optional<TripGroups> tripGroups = tripGroupsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(tripGroups);
    }

    /**
     * {@code DELETE  /trip-groups/:id} : delete the "id" tripGroups.
     *
     * @param id the id of the tripGroups to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trip-groups/{id}")
    public ResponseEntity<Void> deleteTripGroups(@PathVariable Long id) {
        log.debug("REST request to delete TripGroups : {}", id);
        tripGroupsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
