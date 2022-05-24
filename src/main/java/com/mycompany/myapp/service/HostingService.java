package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Hosting;
import com.mycompany.myapp.repository.HostingRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hosting}.
 */
@Service
@Transactional
public class HostingService {

    private final Logger log = LoggerFactory.getLogger(HostingService.class);

    private final HostingRepository hostingRepository;

    public HostingService(HostingRepository hostingRepository) {
        this.hostingRepository = hostingRepository;
    }

    /**
     * Save a hosting.
     *
     * @param hosting the entity to save.
     * @return the persisted entity.
     */
    public Hosting save(Hosting hosting) {
        log.debug("Request to save Hosting : {}", hosting);
        return hostingRepository.save(hosting);
    }

    /**
     * Update a hosting.
     *
     * @param hosting the entity to save.
     * @return the persisted entity.
     */
    public Hosting update(Hosting hosting) {
        log.debug("Request to save Hosting : {}", hosting);
        return hostingRepository.save(hosting);
    }

    /**
     * Partially update a hosting.
     *
     * @param hosting the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hosting> partialUpdate(Hosting hosting) {
        log.debug("Request to partially update Hosting : {}", hosting);

        return hostingRepository
            .findById(hosting.getId())
            .map(existingHosting -> {
                if (hosting.getDate() != null) {
                    existingHosting.setDate(hosting.getDate());
                }

                return existingHosting;
            })
            .map(hostingRepository::save);
    }

    /**
     * Get all the hostings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Hosting> findAll() {
        log.debug("Request to get all Hostings");
        return hostingRepository.findAll();
    }

    /**
     * Get one hosting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hosting> findOne(Long id) {
        log.debug("Request to get Hosting : {}", id);
        return hostingRepository.findById(id);
    }

    /**
     * Delete the hosting by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hosting : {}", id);
        hostingRepository.deleteById(id);
    }
}
