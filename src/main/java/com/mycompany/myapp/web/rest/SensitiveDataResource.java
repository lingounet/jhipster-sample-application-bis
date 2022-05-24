package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SensitiveData;
import com.mycompany.myapp.repository.SensitiveDataRepository;
import com.mycompany.myapp.service.SensitiveDataService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SensitiveData}.
 */
@RestController
@RequestMapping("/api")
public class SensitiveDataResource {

    private final Logger log = LoggerFactory.getLogger(SensitiveDataResource.class);

    private static final String ENTITY_NAME = "sensitiveData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensitiveDataService sensitiveDataService;

    private final SensitiveDataRepository sensitiveDataRepository;

    public SensitiveDataResource(SensitiveDataService sensitiveDataService, SensitiveDataRepository sensitiveDataRepository) {
        this.sensitiveDataService = sensitiveDataService;
        this.sensitiveDataRepository = sensitiveDataRepository;
    }

    /**
     * {@code POST  /sensitive-data} : Create a new sensitiveData.
     *
     * @param sensitiveData the sensitiveData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensitiveData, or with status {@code 400 (Bad Request)} if the sensitiveData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensitive-data")
    public ResponseEntity<SensitiveData> createSensitiveData(@RequestBody SensitiveData sensitiveData) throws URISyntaxException {
        log.debug("REST request to save SensitiveData : {}", sensitiveData);
        if (sensitiveData.getId() != null) {
            throw new BadRequestAlertException("A new sensitiveData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensitiveData result = sensitiveDataService.save(sensitiveData);
        return ResponseEntity
            .created(new URI("/api/sensitive-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensitive-data/:id} : Updates an existing sensitiveData.
     *
     * @param id the id of the sensitiveData to save.
     * @param sensitiveData the sensitiveData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensitiveData,
     * or with status {@code 400 (Bad Request)} if the sensitiveData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensitiveData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensitive-data/{id}")
    public ResponseEntity<SensitiveData> updateSensitiveData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensitiveData sensitiveData
    ) throws URISyntaxException {
        log.debug("REST request to update SensitiveData : {}, {}", id, sensitiveData);
        if (sensitiveData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensitiveData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensitiveDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SensitiveData result = sensitiveDataService.update(sensitiveData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensitiveData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensitive-data/:id} : Partial updates given fields of an existing sensitiveData, field will ignore if it is null
     *
     * @param id the id of the sensitiveData to save.
     * @param sensitiveData the sensitiveData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensitiveData,
     * or with status {@code 400 (Bad Request)} if the sensitiveData is not valid,
     * or with status {@code 404 (Not Found)} if the sensitiveData is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensitiveData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensitive-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SensitiveData> partialUpdateSensitiveData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensitiveData sensitiveData
    ) throws URISyntaxException {
        log.debug("REST request to partial update SensitiveData partially : {}, {}", id, sensitiveData);
        if (sensitiveData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensitiveData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensitiveDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensitiveData> result = sensitiveDataService.partialUpdate(sensitiveData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensitiveData.getId().toString())
        );
    }

    /**
     * {@code GET  /sensitive-data} : get all the sensitiveData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensitiveData in body.
     */
    @GetMapping("/sensitive-data")
    public List<SensitiveData> getAllSensitiveData() {
        log.debug("REST request to get all SensitiveData");
        return sensitiveDataService.findAll();
    }

    /**
     * {@code GET  /sensitive-data/:id} : get the "id" sensitiveData.
     *
     * @param id the id of the sensitiveData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensitiveData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensitive-data/{id}")
    public ResponseEntity<SensitiveData> getSensitiveData(@PathVariable Long id) {
        log.debug("REST request to get SensitiveData : {}", id);
        Optional<SensitiveData> sensitiveData = sensitiveDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensitiveData);
    }

    /**
     * {@code DELETE  /sensitive-data/:id} : delete the "id" sensitiveData.
     *
     * @param id the id of the sensitiveData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensitive-data/{id}")
    public ResponseEntity<Void> deleteSensitiveData(@PathVariable Long id) {
        log.debug("REST request to delete SensitiveData : {}", id);
        sensitiveDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
