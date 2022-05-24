package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Identity;
import com.mycompany.myapp.repository.IdentityRepository;
import com.mycompany.myapp.service.IdentityService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Identity}.
 */
@RestController
@RequestMapping("/api")
public class IdentityResource {

    private final Logger log = LoggerFactory.getLogger(IdentityResource.class);

    private static final String ENTITY_NAME = "identity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentityService identityService;

    private final IdentityRepository identityRepository;

    public IdentityResource(IdentityService identityService, IdentityRepository identityRepository) {
        this.identityService = identityService;
        this.identityRepository = identityRepository;
    }

    /**
     * {@code POST  /identities} : Create a new identity.
     *
     * @param identity the identity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identity, or with status {@code 400 (Bad Request)} if the identity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/identities")
    public ResponseEntity<Identity> createIdentity(@Valid @RequestBody Identity identity) throws URISyntaxException {
        log.debug("REST request to save Identity : {}", identity);
        if (identity.getId() != null) {
            throw new BadRequestAlertException("A new identity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Identity result = identityService.save(identity);
        return ResponseEntity
            .created(new URI("/api/identities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /identities/:id} : Updates an existing identity.
     *
     * @param id the id of the identity to save.
     * @param identity the identity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identity,
     * or with status {@code 400 (Bad Request)} if the identity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/identities/{id}")
    public ResponseEntity<Identity> updateIdentity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Identity identity
    ) throws URISyntaxException {
        log.debug("REST request to update Identity : {}, {}", id, identity);
        if (identity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Identity result = identityService.update(identity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /identities/:id} : Partial updates given fields of an existing identity, field will ignore if it is null
     *
     * @param id the id of the identity to save.
     * @param identity the identity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identity,
     * or with status {@code 400 (Bad Request)} if the identity is not valid,
     * or with status {@code 404 (Not Found)} if the identity is not found,
     * or with status {@code 500 (Internal Server Error)} if the identity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/identities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Identity> partialUpdateIdentity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Identity identity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Identity partially : {}, {}", id, identity);
        if (identity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Identity> result = identityService.partialUpdate(identity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identity.getId().toString())
        );
    }

    /**
     * {@code GET  /identities} : get all the identities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of identities in body.
     */
    @GetMapping("/identities")
    public List<Identity> getAllIdentities() {
        log.debug("REST request to get all Identities");
        return identityService.findAll();
    }

    /**
     * {@code GET  /identities/:id} : get the "id" identity.
     *
     * @param id the id of the identity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/identities/{id}")
    public ResponseEntity<Identity> getIdentity(@PathVariable Long id) {
        log.debug("REST request to get Identity : {}", id);
        Optional<Identity> identity = identityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(identity);
    }

    /**
     * {@code DELETE  /identities/:id} : delete the "id" identity.
     *
     * @param id the id of the identity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/identities/{id}")
    public ResponseEntity<Void> deleteIdentity(@PathVariable Long id) {
        log.debug("REST request to delete Identity : {}", id);
        identityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
