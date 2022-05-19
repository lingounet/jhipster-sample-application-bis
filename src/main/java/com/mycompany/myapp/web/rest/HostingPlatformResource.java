package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HostingPlatform;
import com.mycompany.myapp.repository.HostingPlatformRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HostingPlatform}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HostingPlatformResource {

    private final Logger log = LoggerFactory.getLogger(HostingPlatformResource.class);

    private static final String ENTITY_NAME = "hostingPlatform";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HostingPlatformRepository hostingPlatformRepository;

    public HostingPlatformResource(HostingPlatformRepository hostingPlatformRepository) {
        this.hostingPlatformRepository = hostingPlatformRepository;
    }

    /**
     * {@code POST  /hosting-platforms} : Create a new hostingPlatform.
     *
     * @param hostingPlatform the hostingPlatform to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hostingPlatform, or with status {@code 400 (Bad Request)} if the hostingPlatform has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hosting-platforms")
    public ResponseEntity<HostingPlatform> createHostingPlatform(@RequestBody HostingPlatform hostingPlatform) throws URISyntaxException {
        log.debug("REST request to save HostingPlatform : {}", hostingPlatform);
        if (hostingPlatform.getId() != null) {
            throw new BadRequestAlertException("A new hostingPlatform cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HostingPlatform result = hostingPlatformRepository.save(hostingPlatform);
        return ResponseEntity
            .created(new URI("/api/hosting-platforms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hosting-platforms/:id} : Updates an existing hostingPlatform.
     *
     * @param id the id of the hostingPlatform to save.
     * @param hostingPlatform the hostingPlatform to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hostingPlatform,
     * or with status {@code 400 (Bad Request)} if the hostingPlatform is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hostingPlatform couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hosting-platforms/{id}")
    public ResponseEntity<HostingPlatform> updateHostingPlatform(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HostingPlatform hostingPlatform
    ) throws URISyntaxException {
        log.debug("REST request to update HostingPlatform : {}, {}", id, hostingPlatform);
        if (hostingPlatform.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hostingPlatform.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostingPlatformRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HostingPlatform result = hostingPlatformRepository.save(hostingPlatform);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hostingPlatform.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hosting-platforms/:id} : Partial updates given fields of an existing hostingPlatform, field will ignore if it is null
     *
     * @param id the id of the hostingPlatform to save.
     * @param hostingPlatform the hostingPlatform to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hostingPlatform,
     * or with status {@code 400 (Bad Request)} if the hostingPlatform is not valid,
     * or with status {@code 404 (Not Found)} if the hostingPlatform is not found,
     * or with status {@code 500 (Internal Server Error)} if the hostingPlatform couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hosting-platforms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HostingPlatform> partialUpdateHostingPlatform(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HostingPlatform hostingPlatform
    ) throws URISyntaxException {
        log.debug("REST request to partial update HostingPlatform partially : {}, {}", id, hostingPlatform);
        if (hostingPlatform.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hostingPlatform.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hostingPlatformRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HostingPlatform> result = hostingPlatformRepository
            .findById(hostingPlatform.getId())
            .map(existingHostingPlatform -> {
                if (hostingPlatform.getName() != null) {
                    existingHostingPlatform.setName(hostingPlatform.getName());
                }

                return existingHostingPlatform;
            })
            .map(hostingPlatformRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hostingPlatform.getId().toString())
        );
    }

    /**
     * {@code GET  /hosting-platforms} : get all the hostingPlatforms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hostingPlatforms in body.
     */
    @GetMapping("/hosting-platforms")
    public List<HostingPlatform> getAllHostingPlatforms() {
        log.debug("REST request to get all HostingPlatforms");
        return hostingPlatformRepository.findAll();
    }

    /**
     * {@code GET  /hosting-platforms/:id} : get the "id" hostingPlatform.
     *
     * @param id the id of the hostingPlatform to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hostingPlatform, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hosting-platforms/{id}")
    public ResponseEntity<HostingPlatform> getHostingPlatform(@PathVariable Long id) {
        log.debug("REST request to get HostingPlatform : {}", id);
        Optional<HostingPlatform> hostingPlatform = hostingPlatformRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hostingPlatform);
    }

    /**
     * {@code DELETE  /hosting-platforms/:id} : delete the "id" hostingPlatform.
     *
     * @param id the id of the hostingPlatform to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hosting-platforms/{id}")
    public ResponseEntity<Void> deleteHostingPlatform(@PathVariable Long id) {
        log.debug("REST request to delete HostingPlatform : {}", id);
        hostingPlatformRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
