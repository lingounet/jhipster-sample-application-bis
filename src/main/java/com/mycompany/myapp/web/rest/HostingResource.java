package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Hosting;
import com.mycompany.myapp.repository.HostingRepository;
import com.mycompany.myapp.service.HostingService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Hosting}.
 */
@RestController
@RequestMapping("/api")
public class HostingResource {

    private final Logger log = LoggerFactory.getLogger(HostingResource.class);

    private static final String ENTITY_NAME = "hosting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HostingService hostingService;

    private final HostingRepository hostingRepository;

    public HostingResource(HostingService hostingService, HostingRepository hostingRepository) {
        this.hostingService = hostingService;
        this.hostingRepository = hostingRepository;
    }

    /**
     * {@code POST  /hostings} : Create a new hosting.
     *
     * @param hosting the hosting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hosting, or with status {@code 400 (Bad Request)} if the hosting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hostings")
    public ResponseEntity<Hosting> createHosting(@RequestBody Hosting hosting) throws URISyntaxException {
        log.debug("REST request to save Hosting : {}", hosting);
        if (hosting.getId() != null) {
            throw new BadRequestAlertException("A new hosting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hosting result = hostingService.save(hosting);
        return ResponseEntity
            .created(new URI("/api/hostings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hostings/:id} : Updates an existing hosting.
     *
     * @param id the id of the hosting to save.
     * @param hosting the hosting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hosting,
     * or with status {@code 400 (Bad Request)} if the hosting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hosting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hostings/{id}")
    public ResponseEntity<Hosting> updateHosting(@PathVariable(value = "id", required = false) final Long id, @RequestBody Hosting hosting)
        throws URISyntaxException {
        log.debug("REST request to update Hosting : {}, {}", id, hosting);
        if (hosting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hosting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hosting result = hostingService.update(hosting);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hosting.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hostings/:id} : Partial updates given fields of an existing hosting, field will ignore if it is null
     *
     * @param id the id of the hosting to save.
     * @param hosting the hosting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hosting,
     * or with status {@code 400 (Bad Request)} if the hosting is not valid,
     * or with status {@code 404 (Not Found)} if the hosting is not found,
     * or with status {@code 500 (Internal Server Error)} if the hosting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hostings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hosting> partialUpdateHosting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Hosting hosting
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hosting partially : {}, {}", id, hosting);
        if (hosting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hosting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hosting> result = hostingService.partialUpdate(hosting);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hosting.getId().toString())
        );
    }

    /**
     * {@code GET  /hostings} : get all the hostings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hostings in body.
     */
    @GetMapping("/hostings")
    public List<Hosting> getAllHostings() {
        log.debug("REST request to get all Hostings");
        return hostingService.findAll();
    }

    /**
     * {@code GET  /hostings/:id} : get the "id" hosting.
     *
     * @param id the id of the hosting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hosting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hostings/{id}")
    public ResponseEntity<Hosting> getHosting(@PathVariable Long id) {
        log.debug("REST request to get Hosting : {}", id);
        Optional<Hosting> hosting = hostingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hosting);
    }

    /**
     * {@code DELETE  /hostings/:id} : delete the "id" hosting.
     *
     * @param id the id of the hosting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hostings/{id}")
    public ResponseEntity<Void> deleteHosting(@PathVariable Long id) {
        log.debug("REST request to delete Hosting : {}", id);
        hostingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
