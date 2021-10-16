package com.maria.trip.web.rest;

import com.maria.trip.domain.Tripdetail;
import com.maria.trip.repository.TripdetailRepository;
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
 * REST controller for managing {@link com.maria.trip.domain.Tripdetail}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripdetailResource {

    private final Logger log = LoggerFactory.getLogger(TripdetailResource.class);

    private static final String ENTITY_NAME = "tripdetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripdetailRepository tripdetailRepository;

    public TripdetailResource(TripdetailRepository tripdetailRepository) {
        this.tripdetailRepository = tripdetailRepository;
    }

    /**
     * {@code POST  /tripdetails} : Create a new tripdetail.
     *
     * @param tripdetail the tripdetail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripdetail, or with status {@code 400 (Bad Request)} if the tripdetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tripdetails")
    public ResponseEntity<Tripdetail> createTripdetail(@RequestBody Tripdetail tripdetail) throws URISyntaxException {
        log.debug("REST request to save Tripdetail : {}", tripdetail);
        if (tripdetail.getId() != null) {
            throw new BadRequestAlertException("A new tripdetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tripdetail result = tripdetailRepository.save(tripdetail);
        return ResponseEntity
            .created(new URI("/api/tripdetails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tripdetails/:id} : Updates an existing tripdetail.
     *
     * @param id the id of the tripdetail to save.
     * @param tripdetail the tripdetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripdetail,
     * or with status {@code 400 (Bad Request)} if the tripdetail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripdetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tripdetails/{id}")
    public ResponseEntity<Tripdetail> updateTripdetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tripdetail tripdetail
    ) throws URISyntaxException {
        log.debug("REST request to update Tripdetail : {}, {}", id, tripdetail);
        if (tripdetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripdetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripdetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tripdetail result = tripdetailRepository.save(tripdetail);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripdetail.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tripdetails/:id} : Partial updates given fields of an existing tripdetail, field will ignore if it is null
     *
     * @param id the id of the tripdetail to save.
     * @param tripdetail the tripdetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripdetail,
     * or with status {@code 400 (Bad Request)} if the tripdetail is not valid,
     * or with status {@code 404 (Not Found)} if the tripdetail is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripdetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tripdetails/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tripdetail> partialUpdateTripdetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tripdetail tripdetail
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tripdetail partially : {}, {}", id, tripdetail);
        if (tripdetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripdetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripdetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tripdetail> result = tripdetailRepository
            .findById(tripdetail.getId())
            .map(existingTripdetail -> {
                if (tripdetail.getMinimumList() != null) {
                    existingTripdetail.setMinimumList(tripdetail.getMinimumList());
                }
                if (tripdetail.getMaximumList() != null) {
                    existingTripdetail.setMaximumList(tripdetail.getMaximumList());
                }
                if (tripdetail.getCreatedDate() != null) {
                    existingTripdetail.setCreatedDate(tripdetail.getCreatedDate());
                }
                if (tripdetail.getLastupdated() != null) {
                    existingTripdetail.setLastupdated(tripdetail.getLastupdated());
                }
                if (tripdetail.getDepartureDate() != null) {
                    existingTripdetail.setDepartureDate(tripdetail.getDepartureDate());
                }
                if (tripdetail.getArrivalDate() != null) {
                    existingTripdetail.setArrivalDate(tripdetail.getArrivalDate());
                }
                if (tripdetail.getContentDate() != null) {
                    existingTripdetail.setContentDate(tripdetail.getContentDate());
                }

                return existingTripdetail;
            })
            .map(tripdetailRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripdetail.getId().toString())
        );
    }

    /**
     * {@code GET  /tripdetails} : get all the tripdetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripdetails in body.
     */
    @GetMapping("/tripdetails")
    public List<Tripdetail> getAllTripdetails() {
        log.debug("REST request to get all Tripdetails");
        return tripdetailRepository.findAll();
    }

    /**
     * {@code GET  /tripdetails/:id} : get the "id" tripdetail.
     *
     * @param id the id of the tripdetail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripdetail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tripdetails/{id}")
    public ResponseEntity<Tripdetail> getTripdetail(@PathVariable Long id) {
        log.debug("REST request to get Tripdetail : {}", id);
        Optional<Tripdetail> tripdetail = tripdetailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tripdetail);
    }

    /**
     * {@code DELETE  /tripdetails/:id} : delete the "id" tripdetail.
     *
     * @param id the id of the tripdetail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tripdetails/{id}")
    public ResponseEntity<Void> deleteTripdetail(@PathVariable Long id) {
        log.debug("REST request to delete Tripdetail : {}", id);
        tripdetailRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
