package com.maria.trip.web.rest;

import com.maria.trip.domain.AdressList;
import com.maria.trip.repository.AdressListRepository;
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
 * REST controller for managing {@link com.maria.trip.domain.AdressList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AdressListResource {

    private final Logger log = LoggerFactory.getLogger(AdressListResource.class);

    private static final String ENTITY_NAME = "adressList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdressListRepository adressListRepository;

    public AdressListResource(AdressListRepository adressListRepository) {
        this.adressListRepository = adressListRepository;
    }

    /**
     * {@code POST  /adress-lists} : Create a new adressList.
     *
     * @param adressList the adressList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adressList, or with status {@code 400 (Bad Request)} if the adressList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adress-lists")
    public ResponseEntity<AdressList> createAdressList(@RequestBody AdressList adressList) throws URISyntaxException {
        log.debug("REST request to save AdressList : {}", adressList);
        if (adressList.getId() != null) {
            throw new BadRequestAlertException("A new adressList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdressList result = adressListRepository.save(adressList);
        return ResponseEntity
            .created(new URI("/api/adress-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adress-lists/:id} : Updates an existing adressList.
     *
     * @param id the id of the adressList to save.
     * @param adressList the adressList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adressList,
     * or with status {@code 400 (Bad Request)} if the adressList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adressList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adress-lists/{id}")
    public ResponseEntity<AdressList> updateAdressList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdressList adressList
    ) throws URISyntaxException {
        log.debug("REST request to update AdressList : {}, {}", id, adressList);
        if (adressList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adressList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adressListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdressList result = adressListRepository.save(adressList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adressList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /adress-lists/:id} : Partial updates given fields of an existing adressList, field will ignore if it is null
     *
     * @param id the id of the adressList to save.
     * @param adressList the adressList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adressList,
     * or with status {@code 400 (Bad Request)} if the adressList is not valid,
     * or with status {@code 404 (Not Found)} if the adressList is not found,
     * or with status {@code 500 (Internal Server Error)} if the adressList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/adress-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdressList> partialUpdateAdressList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdressList adressList
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdressList partially : {}, {}", id, adressList);
        if (adressList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adressList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adressListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdressList> result = adressListRepository
            .findById(adressList.getId())
            .map(existingAdressList -> {
                if (adressList.getStreet() != null) {
                    existingAdressList.setStreet(adressList.getStreet());
                }
                if (adressList.getStreetSuite() != null) {
                    existingAdressList.setStreetSuite(adressList.getStreetSuite());
                }
                if (adressList.getPostalCode() != null) {
                    existingAdressList.setPostalCode(adressList.getPostalCode());
                }
                if (adressList.getState() != null) {
                    existingAdressList.setState(adressList.getState());
                }
                if (adressList.getCountry() != null) {
                    existingAdressList.setCountry(adressList.getCountry());
                }
                if (adressList.getLatitude() != null) {
                    existingAdressList.setLatitude(adressList.getLatitude());
                }
                if (adressList.getLongitude() != null) {
                    existingAdressList.setLongitude(adressList.getLongitude());
                }

                return existingAdressList;
            })
            .map(adressListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adressList.getId().toString())
        );
    }

    /**
     * {@code GET  /adress-lists} : get all the adressLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adressLists in body.
     */
    @GetMapping("/adress-lists")
    public List<AdressList> getAllAdressLists() {
        log.debug("REST request to get all AdressLists");
        return adressListRepository.findAll();
    }

    /**
     * {@code GET  /adress-lists/:id} : get the "id" adressList.
     *
     * @param id the id of the adressList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adressList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adress-lists/{id}")
    public ResponseEntity<AdressList> getAdressList(@PathVariable Long id) {
        log.debug("REST request to get AdressList : {}", id);
        Optional<AdressList> adressList = adressListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(adressList);
    }

    /**
     * {@code DELETE  /adress-lists/:id} : delete the "id" adressList.
     *
     * @param id the id of the adressList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adress-lists/{id}")
    public ResponseEntity<Void> deleteAdressList(@PathVariable Long id) {
        log.debug("REST request to delete AdressList : {}", id);
        adressListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
