package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.IcrfStatus;
import com.mycompany.myapp.repository.IcrfStatusRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.IcrfStatus}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IcrfStatusResource {

    private final Logger log = LoggerFactory.getLogger(IcrfStatusResource.class);

    private static final String ENTITY_NAME = "icrfStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IcrfStatusRepository icrfStatusRepository;

    public IcrfStatusResource(IcrfStatusRepository icrfStatusRepository) {
        this.icrfStatusRepository = icrfStatusRepository;
    }

    /**
     * {@code POST  /icrf-statuses} : Create a new icrfStatus.
     *
     * @param icrfStatus the icrfStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new icrfStatus, or with status {@code 400 (Bad Request)} if the icrfStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/icrf-statuses")
    public ResponseEntity<IcrfStatus> createIcrfStatus(@RequestBody IcrfStatus icrfStatus) throws URISyntaxException {
        log.debug("REST request to save IcrfStatus : {}", icrfStatus);
        if (icrfStatus.getId() != null) {
            throw new BadRequestAlertException("A new icrfStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IcrfStatus result = icrfStatusRepository.save(icrfStatus);
        return ResponseEntity
            .created(new URI("/api/icrf-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /icrf-statuses/:id} : Updates an existing icrfStatus.
     *
     * @param id the id of the icrfStatus to save.
     * @param icrfStatus the icrfStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icrfStatus,
     * or with status {@code 400 (Bad Request)} if the icrfStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the icrfStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/icrf-statuses/{id}")
    public ResponseEntity<IcrfStatus> updateIcrfStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IcrfStatus icrfStatus
    ) throws URISyntaxException {
        log.debug("REST request to update IcrfStatus : {}, {}", id, icrfStatus);
        if (icrfStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icrfStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icrfStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IcrfStatus result = icrfStatusRepository.save(icrfStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icrfStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /icrf-statuses/:id} : Partial updates given fields of an existing icrfStatus, field will ignore if it is null
     *
     * @param id the id of the icrfStatus to save.
     * @param icrfStatus the icrfStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icrfStatus,
     * or with status {@code 400 (Bad Request)} if the icrfStatus is not valid,
     * or with status {@code 404 (Not Found)} if the icrfStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the icrfStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/icrf-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IcrfStatus> partialUpdateIcrfStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IcrfStatus icrfStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update IcrfStatus partially : {}, {}", id, icrfStatus);
        if (icrfStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icrfStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icrfStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IcrfStatus> result = icrfStatusRepository
            .findById(icrfStatus.getId())
            .map(existingIcrfStatus -> {
                if (icrfStatus.getName() != null) {
                    existingIcrfStatus.setName(icrfStatus.getName());
                }

                return existingIcrfStatus;
            })
            .map(icrfStatusRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icrfStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /icrf-statuses} : get all the icrfStatuses.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of icrfStatuses in body.
     */
    @GetMapping("/icrf-statuses")
    public List<IcrfStatus> getAllIcrfStatuses(@RequestParam(required = false) String filter) {
        if ("icrf-is-null".equals(filter)) {
            log.debug("REST request to get all IcrfStatuss where icrf is null");
            return StreamSupport
                .stream(icrfStatusRepository.findAll().spliterator(), false)
                .filter(icrfStatus -> icrfStatus.getIcrf() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all IcrfStatuses");
        return icrfStatusRepository.findAll();
    }

    /**
     * {@code GET  /icrf-statuses/:id} : get the "id" icrfStatus.
     *
     * @param id the id of the icrfStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the icrfStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/icrf-statuses/{id}")
    public ResponseEntity<IcrfStatus> getIcrfStatus(@PathVariable Long id) {
        log.debug("REST request to get IcrfStatus : {}", id);
        Optional<IcrfStatus> icrfStatus = icrfStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(icrfStatus);
    }

    /**
     * {@code DELETE  /icrf-statuses/:id} : delete the "id" icrfStatus.
     *
     * @param id the id of the icrfStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/icrf-statuses/{id}")
    public ResponseEntity<Void> deleteIcrfStatus(@PathVariable Long id) {
        log.debug("REST request to delete IcrfStatus : {}", id);
        icrfStatusRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
