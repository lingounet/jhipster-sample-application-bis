package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Aml;
import com.mycompany.myapp.repository.AmlRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Aml}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AmlResource {

    private final Logger log = LoggerFactory.getLogger(AmlResource.class);

    private static final String ENTITY_NAME = "aml";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmlRepository amlRepository;

    public AmlResource(AmlRepository amlRepository) {
        this.amlRepository = amlRepository;
    }

    /**
     * {@code POST  /amls} : Create a new aml.
     *
     * @param aml the aml to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aml, or with status {@code 400 (Bad Request)} if the aml has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amls")
    public ResponseEntity<Aml> createAml(@RequestBody Aml aml) throws URISyntaxException {
        log.debug("REST request to save Aml : {}", aml);
        if (aml.getId() != null) {
            throw new BadRequestAlertException("A new aml cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aml result = amlRepository.save(aml);
        return ResponseEntity
            .created(new URI("/api/amls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amls/:id} : Updates an existing aml.
     *
     * @param id the id of the aml to save.
     * @param aml the aml to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aml,
     * or with status {@code 400 (Bad Request)} if the aml is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aml couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amls/{id}")
    public ResponseEntity<Aml> updateAml(@PathVariable(value = "id", required = false) final Long id, @RequestBody Aml aml)
        throws URISyntaxException {
        log.debug("REST request to update Aml : {}, {}", id, aml);
        if (aml.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aml.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Aml result = amlRepository.save(aml);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aml.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amls/:id} : Partial updates given fields of an existing aml, field will ignore if it is null
     *
     * @param id the id of the aml to save.
     * @param aml the aml to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aml,
     * or with status {@code 400 (Bad Request)} if the aml is not valid,
     * or with status {@code 404 (Not Found)} if the aml is not found,
     * or with status {@code 500 (Internal Server Error)} if the aml couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aml> partialUpdateAml(@PathVariable(value = "id", required = false) final Long id, @RequestBody Aml aml)
        throws URISyntaxException {
        log.debug("REST request to partial update Aml partially : {}, {}", id, aml);
        if (aml.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aml.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aml> result = amlRepository
            .findById(aml.getId())
            .map(existingAml -> {
                if (aml.getName() != null) {
                    existingAml.setName(aml.getName());
                }

                return existingAml;
            })
            .map(amlRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aml.getId().toString())
        );
    }

    /**
     * {@code GET  /amls} : get all the amls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amls in body.
     */
    @GetMapping("/amls")
    public List<Aml> getAllAmls() {
        log.debug("REST request to get all Amls");
        return amlRepository.findAll();
    }

    /**
     * {@code GET  /amls/:id} : get the "id" aml.
     *
     * @param id the id of the aml to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aml, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amls/{id}")
    public ResponseEntity<Aml> getAml(@PathVariable Long id) {
        log.debug("REST request to get Aml : {}", id);
        Optional<Aml> aml = amlRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aml);
    }

    /**
     * {@code DELETE  /amls/:id} : delete the "id" aml.
     *
     * @param id the id of the aml to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amls/{id}")
    public ResponseEntity<Void> deleteAml(@PathVariable Long id) {
        log.debug("REST request to delete Aml : {}", id);
        amlRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
