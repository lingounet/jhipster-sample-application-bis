package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Identity;
import com.mycompany.myapp.repository.IdentityRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Identity}.
 */
@Service
@Transactional
public class IdentityService {

    private final Logger log = LoggerFactory.getLogger(IdentityService.class);

    private final IdentityRepository identityRepository;

    public IdentityService(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    /**
     * Save a identity.
     *
     * @param identity the entity to save.
     * @return the persisted entity.
     */
    public Identity save(Identity identity) {
        log.debug("Request to save Identity : {}", identity);
        return identityRepository.save(identity);
    }

    /**
     * Update a identity.
     *
     * @param identity the entity to save.
     * @return the persisted entity.
     */
    public Identity update(Identity identity) {
        log.debug("Request to save Identity : {}", identity);
        return identityRepository.save(identity);
    }

    /**
     * Partially update a identity.
     *
     * @param identity the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Identity> partialUpdate(Identity identity) {
        log.debug("Request to partially update Identity : {}", identity);

        return identityRepository
            .findById(identity.getId())
            .map(existingIdentity -> {
                if (identity.getApplicationName() != null) {
                    existingIdentity.setApplicationName(identity.getApplicationName());
                }
                if (identity.getSo() != null) {
                    existingIdentity.setSo(identity.getSo());
                }
                if (identity.getProcess() != null) {
                    existingIdentity.setProcess(identity.getProcess());
                }

                return existingIdentity;
            })
            .map(identityRepository::save);
    }

    /**
     * Get all the identities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Identity> findAll() {
        log.debug("Request to get all Identities");
        return identityRepository.findAll();
    }

    /**
     * Get one identity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Identity> findOne(Long id) {
        log.debug("Request to get Identity : {}", id);
        return identityRepository.findById(id);
    }

    /**
     * Delete the identity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Identity : {}", id);
        identityRepository.deleteById(id);
    }
}
