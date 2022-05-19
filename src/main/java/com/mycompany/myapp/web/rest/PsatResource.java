package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Psat;
import com.mycompany.myapp.repository.PsatRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Psat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PsatResource {

    private final Logger log = LoggerFactory.getLogger(PsatResource.class);

    private static final String ENTITY_NAME = "psat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PsatRepository psatRepository;

    public PsatResource(PsatRepository psatRepository) {
        this.psatRepository = psatRepository;
    }

    /**
     * {@code POST  /psats} : Create a new psat.
     *
     * @param psat the psat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new psat, or with status {@code 400 (Bad Request)} if the psat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/psats")
    public ResponseEntity<Psat> createPsat(@RequestBody Psat psat) throws URISyntaxException {
        log.debug("REST request to save Psat : {}", psat);
        if (psat.getId() != null) {
            throw new BadRequestAlertException("A new psat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Psat result = psatRepository.save(psat);
        return ResponseEntity
            .created(new URI("/api/psats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /psats/:id} : Updates an existing psat.
     *
     * @param id the id of the psat to save.
     * @param psat the psat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated psat,
     * or with status {@code 400 (Bad Request)} if the psat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the psat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/psats/{id}")
    public ResponseEntity<Psat> updatePsat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Psat psat)
        throws URISyntaxException {
        log.debug("REST request to update Psat : {}, {}", id, psat);
        if (psat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, psat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!psatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Psat result = psatRepository.save(psat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, psat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /psats/:id} : Partial updates given fields of an existing psat, field will ignore if it is null
     *
     * @param id the id of the psat to save.
     * @param psat the psat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated psat,
     * or with status {@code 400 (Bad Request)} if the psat is not valid,
     * or with status {@code 404 (Not Found)} if the psat is not found,
     * or with status {@code 500 (Internal Server Error)} if the psat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/psats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Psat> partialUpdatePsat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Psat psat)
        throws URISyntaxException {
        log.debug("REST request to partial update Psat partially : {}, {}", id, psat);
        if (psat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, psat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!psatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Psat> result = psatRepository
            .findById(psat.getId())
            .map(existingPsat -> {
                if (psat.getAmlId() != null) {
                    existingPsat.setAmlId(psat.getAmlId());
                }
                if (psat.getOwner() != null) {
                    existingPsat.setOwner(psat.getOwner());
                }
                if (psat.getStatus() != null) {
                    existingPsat.setStatus(psat.getStatus());
                }

                return existingPsat;
            })
            .map(psatRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, psat.getId().toString())
        );
    }

    /**
     * {@code GET  /psats} : get all the psats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of psats in body.
     */
    @GetMapping("/psats")
    public List<Psat> getAllPsats() {
        log.debug("REST request to get all Psats");
        return psatRepository.findAll();
    }

    /**
     * {@code GET  /psats/:id} : get the "id" psat.
     *
     * @param id the id of the psat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the psat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/psats/{id}")
    public ResponseEntity<Psat> getPsat(@PathVariable Long id) {
        log.debug("REST request to get Psat : {}", id);
        Optional<Psat> psat = psatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(psat);
    }

    /**
     * {@code DELETE  /psats/:id} : delete the "id" psat.
     *
     * @param id the id of the psat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/psats/{id}")
    public ResponseEntity<Void> deletePsat(@PathVariable Long id) {
        log.debug("REST request to delete Psat : {}", id);
        psatRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
