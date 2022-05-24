package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HostingType;
import com.mycompany.myapp.repository.HostingTypeRepository;
import com.mycompany.myapp.service.HostingTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HostingType}.
 */
@RestController
@RequestMapping("/api")
public class HostingTypeResource {

    private final Logger log = LoggerFactory.getLogger(HostingTypeResource.class);

    private static final String ENTITY_NAME = "hostingType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HostingTypeService hostingTypeService;

    private final HostingTypeRepository hostingTypeRepository;

    public HostingTypeResource(HostingTypeService hostingTypeService, HostingTypeRepository hostingTypeRepository) {
        this.hostingTypeService = hostingTypeService;
        this.hostingTypeRepository = hostingTypeRepository;
    }

    /**
     * {@code POST  /hosting-types} : Create a new hostingType.
     *
     * @param hostingType the hostingType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hostingType, or with status {@code 400 (Bad Request)} if the hostingType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hosting-types")
    public ResponseEntity<HostingType> createHostingType(@RequestBody HostingType hostingType) throws URISyntaxException {
        log.debug("REST request to save HostingType : {}", hostingType);
        if (hostingType.getId() != null) {
            throw new BadRequestAlertException("A new hostingType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HostingType result = hostingTypeService.save(hostingType);
        return ResponseEntity
            .created(new URI("/api/hosting-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hosting-types/:id} : Updates an existing hostingType.
     *
     * @param id the id of the hostingType to save.
     * @param hostingType the hostingType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hostingType,
     * or with status {@code 400 (Bad Request)} if the hostingType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hostingType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hosting-types/{id}")
    public ResponseEntity<HostingType> updateHostingType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HostingType hostingType
    ) throws URISyntaxException {
        log.debug("REST request to update HostingType : {}, {}", id, hostingType);
        if (hostingType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hostingType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostingTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HostingType result = hostingTypeService.update(hostingType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hostingType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hosting-types/:id} : Partial updates given fields of an existing hostingType, field will ignore if it is null
     *
     * @param id the id of the hostingType to save.
     * @param hostingType the hostingType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hostingType,
     * or with status {@code 400 (Bad Request)} if the hostingType is not valid,
     * or with status {@code 404 (Not Found)} if the hostingType is not found,
     * or with status {@code 500 (Internal Server Error)} if the hostingType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hosting-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HostingType> partialUpdateHostingType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HostingType hostingType
    ) throws URISyntaxException {
        log.debug("REST request to partial update HostingType partially : {}, {}", id, hostingType);
        if (hostingType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hostingType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostingTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HostingType> result = hostingTypeService.partialUpdate(hostingType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hostingType.getId().toString())
        );
    }

    /**
     * {@code GET  /hosting-types} : get all the hostingTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hostingTypes in body.
     */
    @GetMapping("/hosting-types")
    public List<HostingType> getAllHostingTypes() {
        log.debug("REST request to get all HostingTypes");
        return hostingTypeService.findAll();
    }

    /**
     * {@code GET  /hosting-types/:id} : get the "id" hostingType.
     *
     * @param id the id of the hostingType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hostingType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hosting-types/{id}")
    public ResponseEntity<HostingType> getHostingType(@PathVariable Long id) {
        log.debug("REST request to get HostingType : {}", id);
        Optional<HostingType> hostingType = hostingTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hostingType);
    }

    /**
     * {@code DELETE  /hosting-types/:id} : delete the "id" hostingType.
     *
     * @param id the id of the hostingType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hosting-types/{id}")
    public ResponseEntity<Void> deleteHostingType(@PathVariable Long id) {
        log.debug("REST request to delete HostingType : {}", id);
        hostingTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
