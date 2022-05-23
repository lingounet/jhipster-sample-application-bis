package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ApplicationType;
import com.mycompany.myapp.repository.ApplicationTypeRepository;
import com.mycompany.myapp.service.ApplicationTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ApplicationType}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationTypeResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationTypeResource.class);

    private static final String ENTITY_NAME = "applicationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationTypeService applicationTypeService;

    private final ApplicationTypeRepository applicationTypeRepository;

    public ApplicationTypeResource(ApplicationTypeService applicationTypeService, ApplicationTypeRepository applicationTypeRepository) {
        this.applicationTypeService = applicationTypeService;
        this.applicationTypeRepository = applicationTypeRepository;
    }

    /**
     * {@code POST  /application-types} : Create a new applicationType.
     *
     * @param applicationType the applicationType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationType, or with status {@code 400 (Bad Request)} if the applicationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-types")
    public ResponseEntity<ApplicationType> createApplicationType(@RequestBody ApplicationType applicationType) throws URISyntaxException {
        log.debug("REST request to save ApplicationType : {}", applicationType);
        if (applicationType.getId() != null) {
            throw new BadRequestAlertException("A new applicationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationType result = applicationTypeService.save(applicationType);
        return ResponseEntity
            .created(new URI("/api/application-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-types/:id} : Updates an existing applicationType.
     *
     * @param id the id of the applicationType to save.
     * @param applicationType the applicationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationType,
     * or with status {@code 400 (Bad Request)} if the applicationType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-types/{id}")
    public ResponseEntity<ApplicationType> updateApplicationType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationType applicationType
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationType : {}, {}", id, applicationType);
        if (applicationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApplicationType result = applicationTypeService.update(applicationType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-types/:id} : Partial updates given fields of an existing applicationType, field will ignore if it is null
     *
     * @param id the id of the applicationType to save.
     * @param applicationType the applicationType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationType,
     * or with status {@code 400 (Bad Request)} if the applicationType is not valid,
     * or with status {@code 404 (Not Found)} if the applicationType is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/application-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationType> partialUpdateApplicationType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationType applicationType
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationType partially : {}, {}", id, applicationType);
        if (applicationType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationType> result = applicationTypeService.partialUpdate(applicationType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationType.getId().toString())
        );
    }

    /**
     * {@code GET  /application-types} : get all the applicationTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationTypes in body.
     */
    @GetMapping("/application-types")
    public List<ApplicationType> getAllApplicationTypes() {
        log.debug("REST request to get all ApplicationTypes");
        return applicationTypeService.findAll();
    }

    /**
     * {@code GET  /application-types/:id} : get the "id" applicationType.
     *
     * @param id the id of the applicationType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-types/{id}")
    public ResponseEntity<ApplicationType> getApplicationType(@PathVariable Long id) {
        log.debug("REST request to get ApplicationType : {}", id);
        Optional<ApplicationType> applicationType = applicationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationType);
    }

    /**
     * {@code DELETE  /application-types/:id} : delete the "id" applicationType.
     *
     * @param id the id of the applicationType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-types/{id}")
    public ResponseEntity<Void> deleteApplicationType(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationType : {}", id);
        applicationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
