package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.HostingType;
import com.mycompany.myapp.repository.HostingTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HostingType}.
 */
@Service
@Transactional
public class HostingTypeService {

    private final Logger log = LoggerFactory.getLogger(HostingTypeService.class);

    private final HostingTypeRepository hostingTypeRepository;

    public HostingTypeService(HostingTypeRepository hostingTypeRepository) {
        this.hostingTypeRepository = hostingTypeRepository;
    }

    /**
     * Save a hostingType.
     *
     * @param hostingType the entity to save.
     * @return the persisted entity.
     */
    public HostingType save(HostingType hostingType) {
        log.debug("Request to save HostingType : {}", hostingType);
        return hostingTypeRepository.save(hostingType);
    }

    /**
     * Update a hostingType.
     *
     * @param hostingType the entity to save.
     * @return the persisted entity.
     */
    public HostingType update(HostingType hostingType) {
        log.debug("Request to save HostingType : {}", hostingType);
        return hostingTypeRepository.save(hostingType);
    }

    /**
     * Partially update a hostingType.
     *
     * @param hostingType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HostingType> partialUpdate(HostingType hostingType) {
        log.debug("Request to partially update HostingType : {}", hostingType);

        return hostingTypeRepository
            .findById(hostingType.getId())
            .map(existingHostingType -> {
                if (hostingType.getName() != null) {
                    existingHostingType.setName(hostingType.getName());
                }

                return existingHostingType;
            })
            .map(hostingTypeRepository::save);
    }

    /**
     * Get all the hostingTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HostingType> findAll() {
        log.debug("Request to get all HostingTypes");
        return hostingTypeRepository.findAll();
    }

    /**
     * Get one hostingType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HostingType> findOne(Long id) {
        log.debug("Request to get HostingType : {}", id);
        return hostingTypeRepository.findById(id);
    }

    /**
     * Delete the hostingType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HostingType : {}", id);
        hostingTypeRepository.deleteById(id);
    }
}
