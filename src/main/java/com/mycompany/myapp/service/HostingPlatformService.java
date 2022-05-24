package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.HostingPlatform;
import com.mycompany.myapp.repository.HostingPlatformRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HostingPlatform}.
 */
@Service
@Transactional
public class HostingPlatformService {

    private final Logger log = LoggerFactory.getLogger(HostingPlatformService.class);

    private final HostingPlatformRepository hostingPlatformRepository;

    public HostingPlatformService(HostingPlatformRepository hostingPlatformRepository) {
        this.hostingPlatformRepository = hostingPlatformRepository;
    }

    /**
     * Save a hostingPlatform.
     *
     * @param hostingPlatform the entity to save.
     * @return the persisted entity.
     */
    public HostingPlatform save(HostingPlatform hostingPlatform) {
        log.debug("Request to save HostingPlatform : {}", hostingPlatform);
        return hostingPlatformRepository.save(hostingPlatform);
    }

    /**
     * Update a hostingPlatform.
     *
     * @param hostingPlatform the entity to save.
     * @return the persisted entity.
     */
    public HostingPlatform update(HostingPlatform hostingPlatform) {
        log.debug("Request to save HostingPlatform : {}", hostingPlatform);
        return hostingPlatformRepository.save(hostingPlatform);
    }

    /**
     * Partially update a hostingPlatform.
     *
     * @param hostingPlatform the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HostingPlatform> partialUpdate(HostingPlatform hostingPlatform) {
        log.debug("Request to partially update HostingPlatform : {}", hostingPlatform);

        return hostingPlatformRepository
            .findById(hostingPlatform.getId())
            .map(existingHostingPlatform -> {
                if (hostingPlatform.getName() != null) {
                    existingHostingPlatform.setName(hostingPlatform.getName());
                }

                return existingHostingPlatform;
            })
            .map(hostingPlatformRepository::save);
    }

    /**
     * Get all the hostingPlatforms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HostingPlatform> findAll() {
        log.debug("Request to get all HostingPlatforms");
        return hostingPlatformRepository.findAll();
    }

    /**
     * Get one hostingPlatform by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HostingPlatform> findOne(Long id) {
        log.debug("Request to get HostingPlatform : {}", id);
        return hostingPlatformRepository.findById(id);
    }

    /**
     * Delete the hostingPlatform by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HostingPlatform : {}", id);
        hostingPlatformRepository.deleteById(id);
    }
}
