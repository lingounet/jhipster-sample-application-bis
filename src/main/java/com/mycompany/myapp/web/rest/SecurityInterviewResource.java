package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SecurityInterview;
import com.mycompany.myapp.repository.SecurityInterviewRepository;
import com.mycompany.myapp.service.SecurityInterviewService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SecurityInterview}.
 */
@RestController
@RequestMapping("/api")
public class SecurityInterviewResource {

    private final Logger log = LoggerFactory.getLogger(SecurityInterviewResource.class);

    private static final String ENTITY_NAME = "securityInterview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurityInterviewService securityInterviewService;

    private final SecurityInterviewRepository securityInterviewRepository;

    public SecurityInterviewResource(
        SecurityInterviewService securityInterviewService,
        SecurityInterviewRepository securityInterviewRepository
    ) {
        this.securityInterviewService = securityInterviewService;
        this.securityInterviewRepository = securityInterviewRepository;
    }

    /**
     * {@code POST  /security-interviews} : Create a new securityInterview.
     *
     * @param securityInterview the securityInterview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securityInterview, or with status {@code 400 (Bad Request)} if the securityInterview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/security-interviews")
    public ResponseEntity<SecurityInterview> createSecurityInterview(@RequestBody SecurityInterview securityInterview)
        throws URISyntaxException {
        log.debug("REST request to save SecurityInterview : {}", securityInterview);
        if (securityInterview.getId() != null) {
            throw new BadRequestAlertException("A new securityInterview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SecurityInterview result = securityInterviewService.save(securityInterview);
        return ResponseEntity
            .created(new URI("/api/security-interviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /security-interviews/:id} : Updates an existing securityInterview.
     *
     * @param id the id of the securityInterview to save.
     * @param securityInterview the securityInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityInterview,
     * or with status {@code 400 (Bad Request)} if the securityInterview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securityInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/security-interviews/{id}")
    public ResponseEntity<SecurityInterview> updateSecurityInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SecurityInterview securityInterview
    ) throws URISyntaxException {
        log.debug("REST request to update SecurityInterview : {}, {}", id, securityInterview);
        if (securityInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityInterviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SecurityInterview result = securityInterviewService.update(securityInterview);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityInterview.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /security-interviews/:id} : Partial updates given fields of an existing securityInterview, field will ignore if it is null
     *
     * @param id the id of the securityInterview to save.
     * @param securityInterview the securityInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityInterview,
     * or with status {@code 400 (Bad Request)} if the securityInterview is not valid,
     * or with status {@code 404 (Not Found)} if the securityInterview is not found,
     * or with status {@code 500 (Internal Server Error)} if the securityInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/security-interviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SecurityInterview> partialUpdateSecurityInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SecurityInterview securityInterview
    ) throws URISyntaxException {
        log.debug("REST request to partial update SecurityInterview partially : {}, {}", id, securityInterview);
        if (securityInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityInterviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SecurityInterview> result = securityInterviewService.partialUpdate(securityInterview);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityInterview.getId().toString())
        );
    }

    /**
     * {@code GET  /security-interviews} : get all the securityInterviews.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securityInterviews in body.
     */
    @GetMapping("/security-interviews")
    public List<SecurityInterview> getAllSecurityInterviews(@RequestParam(required = false) String filter) {
        if ("psat-is-null".equals(filter)) {
            log.debug("REST request to get all SecurityInterviews where psat is null");
            return securityInterviewService.findAllWherePsatIsNull();
        }
        log.debug("REST request to get all SecurityInterviews");
        return securityInterviewService.findAll();
    }

    /**
     * {@code GET  /security-interviews/:id} : get the "id" securityInterview.
     *
     * @param id the id of the securityInterview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securityInterview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/security-interviews/{id}")
    public ResponseEntity<SecurityInterview> getSecurityInterview(@PathVariable Long id) {
        log.debug("REST request to get SecurityInterview : {}", id);
        Optional<SecurityInterview> securityInterview = securityInterviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securityInterview);
    }

    /**
     * {@code DELETE  /security-interviews/:id} : delete the "id" securityInterview.
     *
     * @param id the id of the securityInterview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/security-interviews/{id}")
    public ResponseEntity<Void> deleteSecurityInterview(@PathVariable Long id) {
        log.debug("REST request to delete SecurityInterview : {}", id);
        securityInterviewService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
