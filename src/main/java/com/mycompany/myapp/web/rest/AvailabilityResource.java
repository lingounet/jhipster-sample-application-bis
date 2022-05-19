package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Availability;
import com.mycompany.myapp.repository.AvailabilityRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Availability}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(AvailabilityResource.class);

    private static final String ENTITY_NAME = "availability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityResource(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * {@code POST  /availabilities} : Create a new availability.
     *
     * @param availability the availability to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new availability, or with status {@code 400 (Bad Request)} if the availability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/availabilities")
    public ResponseEntity<Availability> createAvailability(@RequestBody Availability availability) throws URISyntaxException {
        log.debug("REST request to save Availability : {}", availability);
        if (availability.getId() != null) {
            throw new BadRequestAlertException("A new availability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Availability result = availabilityRepository.save(availability);
        return ResponseEntity
            .created(new URI("/api/availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /availabilities/:id} : Updates an existing availability.
     *
     * @param id the id of the availability to save.
     * @param availability the availability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated availability,
     * or with status {@code 400 (Bad Request)} if the availability is not valid,
     * or with status {@code 500 (Internal Server Error)} if the availability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/availabilities/{id}")
    public ResponseEntity<Availability> updateAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Availability availability
    ) throws URISyntaxException {
        log.debug("REST request to update Availability : {}, {}", id, availability);
        if (availability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, availability.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!availabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Availability result = availabilityRepository.save(availability);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, availability.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /availabilities/:id} : Partial updates given fields of an existing availability, field will ignore if it is null
     *
     * @param id the id of the availability to save.
     * @param availability the availability to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated availability,
     * or with status {@code 400 (Bad Request)} if the availability is not valid,
     * or with status {@code 404 (Not Found)} if the availability is not found,
     * or with status {@code 500 (Internal Server Error)} if the availability couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/availabilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Availability> partialUpdateAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Availability availability
    ) throws URISyntaxException {
        log.debug("REST request to partial update Availability partially : {}, {}", id, availability);
        if (availability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, availability.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!availabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Availability> result = availabilityRepository
            .findById(availability.getId())
            .map(existingAvailability -> {
                if (availability.getFinancial() != null) {
                    existingAvailability.setFinancial(availability.getFinancial());
                }
                if (availability.getLegal() != null) {
                    existingAvailability.setLegal(availability.getLegal());
                }
                if (availability.getImage() != null) {
                    existingAvailability.setImage(availability.getImage());
                }
                if (availability.getStrategy() != null) {
                    existingAvailability.setStrategy(availability.getStrategy());
                }
                if (availability.getOperational() != null) {
                    existingAvailability.setOperational(availability.getOperational());
                }
                if (availability.getTraceability() != null) {
                    existingAvailability.setTraceability(availability.getTraceability());
                }
                if (availability.getTraceabilityContentType() != null) {
                    existingAvailability.setTraceabilityContentType(availability.getTraceabilityContentType());
                }
                if (availability.getConfidentiality() != null) {
                    existingAvailability.setConfidentiality(availability.getConfidentiality());
                }
                if (availability.getConfidentialityContentType() != null) {
                    existingAvailability.setConfidentialityContentType(availability.getConfidentialityContentType());
                }
                if (availability.getIntegrity() != null) {
                    existingAvailability.setIntegrity(availability.getIntegrity());
                }
                if (availability.getIntegrityContentType() != null) {
                    existingAvailability.setIntegrityContentType(availability.getIntegrityContentType());
                }
                if (availability.getCritical() != null) {
                    existingAvailability.setCritical(availability.getCritical());
                }

                return existingAvailability;
            })
            .map(availabilityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, availability.getId().toString())
        );
    }

    /**
     * {@code GET  /availabilities} : get all the availabilities.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of availabilities in body.
     */
    @GetMapping("/availabilities")
    public List<Availability> getAllAvailabilities(@RequestParam(required = false) String filter) {
        if ("securityinterview-is-null".equals(filter)) {
            log.debug("REST request to get all Availabilitys where securityInterview is null");
            return StreamSupport
                .stream(availabilityRepository.findAll().spliterator(), false)
                .filter(availability -> availability.getSecurityInterview() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Availabilities");
        return availabilityRepository.findAll();
    }

    /**
     * {@code GET  /availabilities/:id} : get the "id" availability.
     *
     * @param id the id of the availability to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the availability, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/availabilities/{id}")
    public ResponseEntity<Availability> getAvailability(@PathVariable Long id) {
        log.debug("REST request to get Availability : {}", id);
        Optional<Availability> availability = availabilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(availability);
    }

    /**
     * {@code DELETE  /availabilities/:id} : delete the "id" availability.
     *
     * @param id the id of the availability to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/availabilities/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        log.debug("REST request to delete Availability : {}", id);
        availabilityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
