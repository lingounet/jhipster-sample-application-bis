package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.IcrfAssessment;
import com.mycompany.myapp.repository.IcrfAssessmentRepository;
import com.mycompany.myapp.service.IcrfAssessmentService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.IcrfAssessment}.
 */
@RestController
@RequestMapping("/api")
public class IcrfAssessmentResource {

    private final Logger log = LoggerFactory.getLogger(IcrfAssessmentResource.class);

    private static final String ENTITY_NAME = "icrfAssessment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IcrfAssessmentService icrfAssessmentService;

    private final IcrfAssessmentRepository icrfAssessmentRepository;

    public IcrfAssessmentResource(IcrfAssessmentService icrfAssessmentService, IcrfAssessmentRepository icrfAssessmentRepository) {
        this.icrfAssessmentService = icrfAssessmentService;
        this.icrfAssessmentRepository = icrfAssessmentRepository;
    }

    /**
     * {@code POST  /icrf-assessments} : Create a new icrfAssessment.
     *
     * @param icrfAssessment the icrfAssessment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new icrfAssessment, or with status {@code 400 (Bad Request)} if the icrfAssessment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/icrf-assessments")
    public ResponseEntity<IcrfAssessment> createIcrfAssessment(@RequestBody IcrfAssessment icrfAssessment) throws URISyntaxException {
        log.debug("REST request to save IcrfAssessment : {}", icrfAssessment);
        if (icrfAssessment.getId() != null) {
            throw new BadRequestAlertException("A new icrfAssessment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IcrfAssessment result = icrfAssessmentService.save(icrfAssessment);
        return ResponseEntity
            .created(new URI("/api/icrf-assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /icrf-assessments/:id} : Updates an existing icrfAssessment.
     *
     * @param id the id of the icrfAssessment to save.
     * @param icrfAssessment the icrfAssessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icrfAssessment,
     * or with status {@code 400 (Bad Request)} if the icrfAssessment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the icrfAssessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/icrf-assessments/{id}")
    public ResponseEntity<IcrfAssessment> updateIcrfAssessment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IcrfAssessment icrfAssessment
    ) throws URISyntaxException {
        log.debug("REST request to update IcrfAssessment : {}, {}", id, icrfAssessment);
        if (icrfAssessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icrfAssessment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icrfAssessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IcrfAssessment result = icrfAssessmentService.update(icrfAssessment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icrfAssessment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /icrf-assessments/:id} : Partial updates given fields of an existing icrfAssessment, field will ignore if it is null
     *
     * @param id the id of the icrfAssessment to save.
     * @param icrfAssessment the icrfAssessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icrfAssessment,
     * or with status {@code 400 (Bad Request)} if the icrfAssessment is not valid,
     * or with status {@code 404 (Not Found)} if the icrfAssessment is not found,
     * or with status {@code 500 (Internal Server Error)} if the icrfAssessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/icrf-assessments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IcrfAssessment> partialUpdateIcrfAssessment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IcrfAssessment icrfAssessment
    ) throws URISyntaxException {
        log.debug("REST request to partial update IcrfAssessment partially : {}, {}", id, icrfAssessment);
        if (icrfAssessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icrfAssessment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icrfAssessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IcrfAssessment> result = icrfAssessmentService.partialUpdate(icrfAssessment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icrfAssessment.getId().toString())
        );
    }

    /**
     * {@code GET  /icrf-assessments} : get all the icrfAssessments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of icrfAssessments in body.
     */
    @GetMapping("/icrf-assessments")
    public List<IcrfAssessment> getAllIcrfAssessments() {
        log.debug("REST request to get all IcrfAssessments");
        return icrfAssessmentService.findAll();
    }

    /**
     * {@code GET  /icrf-assessments/:id} : get the "id" icrfAssessment.
     *
     * @param id the id of the icrfAssessment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the icrfAssessment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/icrf-assessments/{id}")
    public ResponseEntity<IcrfAssessment> getIcrfAssessment(@PathVariable Long id) {
        log.debug("REST request to get IcrfAssessment : {}", id);
        Optional<IcrfAssessment> icrfAssessment = icrfAssessmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(icrfAssessment);
    }

    /**
     * {@code DELETE  /icrf-assessments/:id} : delete the "id" icrfAssessment.
     *
     * @param id the id of the icrfAssessment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/icrf-assessments/{id}")
    public ResponseEntity<Void> deleteIcrfAssessment(@PathVariable Long id) {
        log.debug("REST request to delete IcrfAssessment : {}", id);
        icrfAssessmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
