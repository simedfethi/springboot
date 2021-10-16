package com.maria.trip.web.rest;

import com.maria.trip.domain.TripCategory;
import com.maria.trip.repository.TripCategoryRepository;
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
 * REST controller for managing {@link com.maria.trip.domain.TripCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripCategoryResource {

    private final Logger log = LoggerFactory.getLogger(TripCategoryResource.class);

    private static final String ENTITY_NAME = "tripCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripCategoryRepository tripCategoryRepository;

    public TripCategoryResource(TripCategoryRepository tripCategoryRepository) {
        this.tripCategoryRepository = tripCategoryRepository;
    }

    /**
     * {@code POST  /trip-categories} : Create a new tripCategory.
     *
     * @param tripCategory the tripCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripCategory, or with status {@code 400 (Bad Request)} if the tripCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trip-categories")
    public ResponseEntity<TripCategory> createTripCategory(@Valid @RequestBody TripCategory tripCategory) throws URISyntaxException {
        log.debug("REST request to save TripCategory : {}", tripCategory);
        if (tripCategory.getId() != null) {
            throw new BadRequestAlertException("A new tripCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripCategory result = tripCategoryRepository.save(tripCategory);
        return ResponseEntity
            .created(new URI("/api/trip-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trip-categories/:id} : Updates an existing tripCategory.
     *
     * @param id the id of the tripCategory to save.
     * @param tripCategory the tripCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripCategory,
     * or with status {@code 400 (Bad Request)} if the tripCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trip-categories/{id}")
    public ResponseEntity<TripCategory> updateTripCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TripCategory tripCategory
    ) throws URISyntaxException {
        log.debug("REST request to update TripCategory : {}, {}", id, tripCategory);
        if (tripCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripCategory result = tripCategoryRepository.save(tripCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trip-categories/:id} : Partial updates given fields of an existing tripCategory, field will ignore if it is null
     *
     * @param id the id of the tripCategory to save.
     * @param tripCategory the tripCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripCategory,
     * or with status {@code 400 (Bad Request)} if the tripCategory is not valid,
     * or with status {@code 404 (Not Found)} if the tripCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trip-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripCategory> partialUpdateTripCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TripCategory tripCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update TripCategory partially : {}, {}", id, tripCategory);
        if (tripCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripCategory> result = tripCategoryRepository
            .findById(tripCategory.getId())
            .map(existingTripCategory -> {
                if (tripCategory.getCategoryname() != null) {
                    existingTripCategory.setCategoryname(tripCategory.getCategoryname());
                }

                return existingTripCategory;
            })
            .map(tripCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-categories} : get all the tripCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripCategories in body.
     */
    @GetMapping("/trip-categories")
    public List<TripCategory> getAllTripCategories() {
        log.debug("REST request to get all TripCategories");
        return tripCategoryRepository.findAll();
    }

    /**
     * {@code GET  /trip-categories/:id} : get the "id" tripCategory.
     *
     * @param id the id of the tripCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trip-categories/{id}")
    public ResponseEntity<TripCategory> getTripCategory(@PathVariable Long id) {
        log.debug("REST request to get TripCategory : {}", id);
        Optional<TripCategory> tripCategory = tripCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tripCategory);
    }

    /**
     * {@code DELETE  /trip-categories/:id} : delete the "id" tripCategory.
     *
     * @param id the id of the tripCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trip-categories/{id}")
    public ResponseEntity<Void> deleteTripCategory(@PathVariable Long id) {
        log.debug("REST request to delete TripCategory : {}", id);
        tripCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
