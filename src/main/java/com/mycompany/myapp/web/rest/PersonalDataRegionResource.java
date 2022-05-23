package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PersonalDataRegion;
import com.mycompany.myapp.repository.PersonalDataRegionRepository;
import com.mycompany.myapp.service.PersonalDataRegionService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PersonalDataRegion}.
 */
@RestController
@RequestMapping("/api")
public class PersonalDataRegionResource {

    private final Logger log = LoggerFactory.getLogger(PersonalDataRegionResource.class);

    private static final String ENTITY_NAME = "personalDataRegion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalDataRegionService personalDataRegionService;

    private final PersonalDataRegionRepository personalDataRegionRepository;

    public PersonalDataRegionResource(
        PersonalDataRegionService personalDataRegionService,
        PersonalDataRegionRepository personalDataRegionRepository
    ) {
        this.personalDataRegionService = personalDataRegionService;
        this.personalDataRegionRepository = personalDataRegionRepository;
    }

    /**
     * {@code POST  /personal-data-regions} : Create a new personalDataRegion.
     *
     * @param personalDataRegion the personalDataRegion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalDataRegion, or with status {@code 400 (Bad Request)} if the personalDataRegion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personal-data-regions")
    public ResponseEntity<PersonalDataRegion> createPersonalDataRegion(@RequestBody PersonalDataRegion personalDataRegion)
        throws URISyntaxException {
        log.debug("REST request to save PersonalDataRegion : {}", personalDataRegion);
        if (personalDataRegion.getId() != null) {
            throw new BadRequestAlertException("A new personalDataRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalDataRegion result = personalDataRegionService.save(personalDataRegion);
        return ResponseEntity
            .created(new URI("/api/personal-data-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personal-data-regions/:id} : Updates an existing personalDataRegion.
     *
     * @param id the id of the personalDataRegion to save.
     * @param personalDataRegion the personalDataRegion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalDataRegion,
     * or with status {@code 400 (Bad Request)} if the personalDataRegion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalDataRegion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personal-data-regions/{id}")
    public ResponseEntity<PersonalDataRegion> updatePersonalDataRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalDataRegion personalDataRegion
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalDataRegion : {}, {}", id, personalDataRegion);
        if (personalDataRegion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalDataRegion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDataRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalDataRegion result = personalDataRegionService.update(personalDataRegion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalDataRegion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personal-data-regions/:id} : Partial updates given fields of an existing personalDataRegion, field will ignore if it is null
     *
     * @param id the id of the personalDataRegion to save.
     * @param personalDataRegion the personalDataRegion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalDataRegion,
     * or with status {@code 400 (Bad Request)} if the personalDataRegion is not valid,
     * or with status {@code 404 (Not Found)} if the personalDataRegion is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalDataRegion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personal-data-regions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalDataRegion> partialUpdatePersonalDataRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalDataRegion personalDataRegion
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalDataRegion partially : {}, {}", id, personalDataRegion);
        if (personalDataRegion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalDataRegion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDataRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalDataRegion> result = personalDataRegionService.partialUpdate(personalDataRegion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalDataRegion.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-data-regions} : get all the personalDataRegions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalDataRegions in body.
     */
    @GetMapping("/personal-data-regions")
    public List<PersonalDataRegion> getAllPersonalDataRegions() {
        log.debug("REST request to get all PersonalDataRegions");
        return personalDataRegionService.findAll();
    }

    /**
     * {@code GET  /personal-data-regions/:id} : get the "id" personalDataRegion.
     *
     * @param id the id of the personalDataRegion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalDataRegion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personal-data-regions/{id}")
    public ResponseEntity<PersonalDataRegion> getPersonalDataRegion(@PathVariable Long id) {
        log.debug("REST request to get PersonalDataRegion : {}", id);
        Optional<PersonalDataRegion> personalDataRegion = personalDataRegionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalDataRegion);
    }

    /**
     * {@code DELETE  /personal-data-regions/:id} : delete the "id" personalDataRegion.
     *
     * @param id the id of the personalDataRegion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personal-data-regions/{id}")
    public ResponseEntity<Void> deletePersonalDataRegion(@PathVariable Long id) {
        log.debug("REST request to delete PersonalDataRegion : {}", id);
        personalDataRegionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
