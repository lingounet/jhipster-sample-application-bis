package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PersonalDataType;
import com.mycompany.myapp.repository.PersonalDataTypeRepository;
import com.mycompany.myapp.service.PersonalDataTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PersonalDataType}.
 */
@RestController
@RequestMapping("/api")
public class PersonalDataTypeResource {

    private final Logger log = LoggerFactory.getLogger(PersonalDataTypeResource.class);

    private static final String ENTITY_NAME = "personalDataType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalDataTypeService personalDataTypeService;

    private final PersonalDataTypeRepository personalDataTypeRepository;

    public PersonalDataTypeResource(
        PersonalDataTypeService personalDataTypeService,
        PersonalDataTypeRepository personalDataTypeRepository
    ) {
        this.personalDataTypeService = personalDataTypeService;
        this.personalDataTypeRepository = personalDataTypeRepository;
    }

    /**
     * {@code POST  /personal-data-types} : Create a new personalDataType.
     *
     * @param personalDataType the personalDataType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalDataType, or with status {@code 400 (Bad Request)} if the personalDataType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personal-data-types")
    public ResponseEntity<PersonalDataType> createPersonalDataType(@RequestBody PersonalDataType personalDataType)
        throws URISyntaxException {
        log.debug("REST request to save PersonalDataType : {}", personalDataType);
        if (personalDataType.getId() != null) {
            throw new BadRequestAlertException("A new personalDataType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalDataType result = personalDataTypeService.save(personalDataType);
        return ResponseEntity
            .created(new URI("/api/personal-data-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personal-data-types/:id} : Updates an existing personalDataType.
     *
     * @param id the id of the personalDataType to save.
     * @param personalDataType the personalDataType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalDataType,
     * or with status {@code 400 (Bad Request)} if the personalDataType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalDataType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personal-data-types/{id}")
    public ResponseEntity<PersonalDataType> updatePersonalDataType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalDataType personalDataType
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalDataType : {}, {}", id, personalDataType);
        if (personalDataType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalDataType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDataTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalDataType result = personalDataTypeService.update(personalDataType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalDataType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personal-data-types/:id} : Partial updates given fields of an existing personalDataType, field will ignore if it is null
     *
     * @param id the id of the personalDataType to save.
     * @param personalDataType the personalDataType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalDataType,
     * or with status {@code 400 (Bad Request)} if the personalDataType is not valid,
     * or with status {@code 404 (Not Found)} if the personalDataType is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalDataType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personal-data-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalDataType> partialUpdatePersonalDataType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalDataType personalDataType
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalDataType partially : {}, {}", id, personalDataType);
        if (personalDataType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalDataType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalDataTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalDataType> result = personalDataTypeService.partialUpdate(personalDataType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalDataType.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-data-types} : get all the personalDataTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalDataTypes in body.
     */
    @GetMapping("/personal-data-types")
    public List<PersonalDataType> getAllPersonalDataTypes() {
        log.debug("REST request to get all PersonalDataTypes");
        return personalDataTypeService.findAll();
    }

    /**
     * {@code GET  /personal-data-types/:id} : get the "id" personalDataType.
     *
     * @param id the id of the personalDataType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalDataType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personal-data-types/{id}")
    public ResponseEntity<PersonalDataType> getPersonalDataType(@PathVariable Long id) {
        log.debug("REST request to get PersonalDataType : {}", id);
        Optional<PersonalDataType> personalDataType = personalDataTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalDataType);
    }

    /**
     * {@code DELETE  /personal-data-types/:id} : delete the "id" personalDataType.
     *
     * @param id the id of the personalDataType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personal-data-types/{id}")
    public ResponseEntity<Void> deletePersonalDataType(@PathVariable Long id) {
        log.debug("REST request to delete PersonalDataType : {}", id);
        personalDataTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
