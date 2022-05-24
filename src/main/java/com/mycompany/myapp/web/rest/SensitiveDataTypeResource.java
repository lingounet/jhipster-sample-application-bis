package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SensitiveDataType;
import com.mycompany.myapp.repository.SensitiveDataTypeRepository;
import com.mycompany.myapp.service.SensitiveDataTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SensitiveDataType}.
 */
@RestController
@RequestMapping("/api")
public class SensitiveDataTypeResource {

    private final Logger log = LoggerFactory.getLogger(SensitiveDataTypeResource.class);

    private static final String ENTITY_NAME = "sensitiveDataType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensitiveDataTypeService sensitiveDataTypeService;

    private final SensitiveDataTypeRepository sensitiveDataTypeRepository;

    public SensitiveDataTypeResource(
        SensitiveDataTypeService sensitiveDataTypeService,
        SensitiveDataTypeRepository sensitiveDataTypeRepository
    ) {
        this.sensitiveDataTypeService = sensitiveDataTypeService;
        this.sensitiveDataTypeRepository = sensitiveDataTypeRepository;
    }

    /**
     * {@code POST  /sensitive-data-types} : Create a new sensitiveDataType.
     *
     * @param sensitiveDataType the sensitiveDataType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensitiveDataType, or with status {@code 400 (Bad Request)} if the sensitiveDataType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensitive-data-types")
    public ResponseEntity<SensitiveDataType> createSensitiveDataType(@RequestBody SensitiveDataType sensitiveDataType)
        throws URISyntaxException {
        log.debug("REST request to save SensitiveDataType : {}", sensitiveDataType);
        if (sensitiveDataType.getId() != null) {
            throw new BadRequestAlertException("A new sensitiveDataType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensitiveDataType result = sensitiveDataTypeService.save(sensitiveDataType);
        return ResponseEntity
            .created(new URI("/api/sensitive-data-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensitive-data-types/:id} : Updates an existing sensitiveDataType.
     *
     * @param id the id of the sensitiveDataType to save.
     * @param sensitiveDataType the sensitiveDataType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensitiveDataType,
     * or with status {@code 400 (Bad Request)} if the sensitiveDataType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensitiveDataType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensitive-data-types/{id}")
    public ResponseEntity<SensitiveDataType> updateSensitiveDataType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensitiveDataType sensitiveDataType
    ) throws URISyntaxException {
        log.debug("REST request to update SensitiveDataType : {}, {}", id, sensitiveDataType);
        if (sensitiveDataType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensitiveDataType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensitiveDataTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SensitiveDataType result = sensitiveDataTypeService.update(sensitiveDataType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensitiveDataType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensitive-data-types/:id} : Partial updates given fields of an existing sensitiveDataType, field will ignore if it is null
     *
     * @param id the id of the sensitiveDataType to save.
     * @param sensitiveDataType the sensitiveDataType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensitiveDataType,
     * or with status {@code 400 (Bad Request)} if the sensitiveDataType is not valid,
     * or with status {@code 404 (Not Found)} if the sensitiveDataType is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensitiveDataType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensitive-data-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SensitiveDataType> partialUpdateSensitiveDataType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensitiveDataType sensitiveDataType
    ) throws URISyntaxException {
        log.debug("REST request to partial update SensitiveDataType partially : {}, {}", id, sensitiveDataType);
        if (sensitiveDataType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensitiveDataType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensitiveDataTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensitiveDataType> result = sensitiveDataTypeService.partialUpdate(sensitiveDataType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensitiveDataType.getId().toString())
        );
    }

    /**
     * {@code GET  /sensitive-data-types} : get all the sensitiveDataTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensitiveDataTypes in body.
     */
    @GetMapping("/sensitive-data-types")
    public List<SensitiveDataType> getAllSensitiveDataTypes() {
        log.debug("REST request to get all SensitiveDataTypes");
        return sensitiveDataTypeService.findAll();
    }

    /**
     * {@code GET  /sensitive-data-types/:id} : get the "id" sensitiveDataType.
     *
     * @param id the id of the sensitiveDataType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensitiveDataType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensitive-data-types/{id}")
    public ResponseEntity<SensitiveDataType> getSensitiveDataType(@PathVariable Long id) {
        log.debug("REST request to get SensitiveDataType : {}", id);
        Optional<SensitiveDataType> sensitiveDataType = sensitiveDataTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensitiveDataType);
    }

    /**
     * {@code DELETE  /sensitive-data-types/:id} : delete the "id" sensitiveDataType.
     *
     * @param id the id of the sensitiveDataType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensitive-data-types/{id}")
    public ResponseEntity<Void> deleteSensitiveDataType(@PathVariable Long id) {
        log.debug("REST request to delete SensitiveDataType : {}", id);
        sensitiveDataTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
