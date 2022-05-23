package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PersonalData;
import com.mycompany.myapp.repository.PersonalDataRepository;
import com.mycompany.myapp.service.PersonalDataService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PersonalData}.
 */
@RestController
@RequestMapping("/api")
public class PersonalDataResource {

    private final Logger log = LoggerFactory.getLogger(PersonalDataResource.class);

    private static final String ENTITY_NAME = "personalData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalDataService personalDataService;

    private final PersonalDataRepository personalDataRepository;

    public PersonalDataResource(PersonalDataService personalDataService, PersonalDataRepository personalDataRepository) {
        this.personalDataService = personalDataService;
        this.personalDataRepository = personalDataRepository;
    }

    /**
     * {@code POST  /personal-data} : Create a new personalData.
     *
     * @param personalData the personalData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalData, or with status {@code 400 (Bad Request)} if the personalData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personal-data")
    public ResponseEntity<PersonalData> createPersonalData(@RequestBody PersonalData personalData) throws URISyntaxException {
        log.debug("REST request to save PersonalData : {}", personalData);
        if (personalData.getId() != null) {
            throw new BadRequestAlertException("A new personalData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalData result = personalDataService.save(personalData);
        return ResponseEntity
            .created(new URI("/api/personal-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personal-data/:id} : Updates an existing personalData.
     *
     * @param id the id of the personalData to save.
     * @param personalData the personalData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalData,
     * or with status {@code 400 (Bad Request)} if the personalData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personal-data/{id}")
    public ResponseEntity<PersonalData> updatePersonalData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalData personalData
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalData : {}, {}", id, personalData);
        if (personalData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalData result = personalDataService.update(personalData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personal-data/:id} : Partial updates given fields of an existing personalData, field will ignore if it is null
     *
     * @param id the id of the personalData to save.
     * @param personalData the personalData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalData,
     * or with status {@code 400 (Bad Request)} if the personalData is not valid,
     * or with status {@code 404 (Not Found)} if the personalData is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personal-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalData> partialUpdatePersonalData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalData personalData
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalData partially : {}, {}", id, personalData);
        if (personalData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalData> result = personalDataService.partialUpdate(personalData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalData.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-data} : get all the personalData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalData in body.
     */
    @GetMapping("/personal-data")
    public List<PersonalData> getAllPersonalData() {
        log.debug("REST request to get all PersonalData");
        return personalDataService.findAll();
    }

    /**
     * {@code GET  /personal-data/:id} : get the "id" personalData.
     *
     * @param id the id of the personalData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personal-data/{id}")
    public ResponseEntity<PersonalData> getPersonalData(@PathVariable Long id) {
        log.debug("REST request to get PersonalData : {}", id);
        Optional<PersonalData> personalData = personalDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalData);
    }

    /**
     * {@code DELETE  /personal-data/:id} : delete the "id" personalData.
     *
     * @param id the id of the personalData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personal-data/{id}")
    public ResponseEntity<Void> deletePersonalData(@PathVariable Long id) {
        log.debug("REST request to delete PersonalData : {}", id);
        personalDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
