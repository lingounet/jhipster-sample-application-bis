package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Icrf;
import com.mycompany.myapp.repository.IcrfRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Icrf}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IcrfResource {

    private final Logger log = LoggerFactory.getLogger(IcrfResource.class);

    private static final String ENTITY_NAME = "icrf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IcrfRepository icrfRepository;

    public IcrfResource(IcrfRepository icrfRepository) {
        this.icrfRepository = icrfRepository;
    }

    /**
     * {@code POST  /icrfs} : Create a new icrf.
     *
     * @param icrf the icrf to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new icrf, or with status {@code 400 (Bad Request)} if the icrf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/icrfs")
    public ResponseEntity<Icrf> createIcrf(@RequestBody Icrf icrf) throws URISyntaxException {
        log.debug("REST request to save Icrf : {}", icrf);
        if (icrf.getId() != null) {
            throw new BadRequestAlertException("A new icrf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Icrf result = icrfRepository.save(icrf);
        return ResponseEntity
            .created(new URI("/api/icrfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /icrfs/:id} : Updates an existing icrf.
     *
     * @param id the id of the icrf to save.
     * @param icrf the icrf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icrf,
     * or with status {@code 400 (Bad Request)} if the icrf is not valid,
     * or with status {@code 500 (Internal Server Error)} if the icrf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/icrfs/{id}")
    public ResponseEntity<Icrf> updateIcrf(@PathVariable(value = "id", required = false) final Long id, @RequestBody Icrf icrf)
        throws URISyntaxException {
        log.debug("REST request to update Icrf : {}, {}", id, icrf);
        if (icrf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icrf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icrfRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Icrf result = icrfRepository.save(icrf);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icrf.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /icrfs/:id} : Partial updates given fields of an existing icrf, field will ignore if it is null
     *
     * @param id the id of the icrf to save.
     * @param icrf the icrf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated icrf,
     * or with status {@code 400 (Bad Request)} if the icrf is not valid,
     * or with status {@code 404 (Not Found)} if the icrf is not found,
     * or with status {@code 500 (Internal Server Error)} if the icrf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/icrfs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Icrf> partialUpdateIcrf(@PathVariable(value = "id", required = false) final Long id, @RequestBody Icrf icrf)
        throws URISyntaxException {
        log.debug("REST request to partial update Icrf partially : {}, {}", id, icrf);
        if (icrf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, icrf.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!icrfRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Icrf> result = icrfRepository
            .findById(icrf.getId())
            .map(existingIcrf -> {
                if (icrf.getCode() != null) {
                    existingIcrf.setCode(icrf.getCode());
                }
                if (icrf.getDescription() != null) {
                    existingIcrf.setDescription(icrf.getDescription());
                }

                return existingIcrf;
            })
            .map(icrfRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, icrf.getId().toString())
        );
    }

    /**
     * {@code GET  /icrfs} : get all the icrfs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of icrfs in body.
     */
    @GetMapping("/icrfs")
    public List<Icrf> getAllIcrfs() {
        log.debug("REST request to get all Icrfs");
        return icrfRepository.findAll();
    }

    /**
     * {@code GET  /icrfs/:id} : get the "id" icrf.
     *
     * @param id the id of the icrf to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the icrf, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/icrfs/{id}")
    public ResponseEntity<Icrf> getIcrf(@PathVariable Long id) {
        log.debug("REST request to get Icrf : {}", id);
        Optional<Icrf> icrf = icrfRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(icrf);
    }

    /**
     * {@code DELETE  /icrfs/:id} : delete the "id" icrf.
     *
     * @param id the id of the icrf to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/icrfs/{id}")
    public ResponseEntity<Void> deleteIcrf(@PathVariable Long id) {
        log.debug("REST request to delete Icrf : {}", id);
        icrfRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
