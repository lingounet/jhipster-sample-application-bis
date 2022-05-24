package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ApplicationType;
import com.mycompany.myapp.repository.ApplicationTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApplicationType}.
 */
@Service
@Transactional
public class ApplicationTypeService {

    private final Logger log = LoggerFactory.getLogger(ApplicationTypeService.class);

    private final ApplicationTypeRepository applicationTypeRepository;

    public ApplicationTypeService(ApplicationTypeRepository applicationTypeRepository) {
        this.applicationTypeRepository = applicationTypeRepository;
    }

    /**
     * Save a applicationType.
     *
     * @param applicationType the entity to save.
     * @return the persisted entity.
     */
    public ApplicationType save(ApplicationType applicationType) {
        log.debug("Request to save ApplicationType : {}", applicationType);
        return applicationTypeRepository.save(applicationType);
    }

    /**
     * Update a applicationType.
     *
     * @param applicationType the entity to save.
     * @return the persisted entity.
     */
    public ApplicationType update(ApplicationType applicationType) {
        log.debug("Request to save ApplicationType : {}", applicationType);
        return applicationTypeRepository.save(applicationType);
    }

    /**
     * Partially update a applicationType.
     *
     * @param applicationType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicationType> partialUpdate(ApplicationType applicationType) {
        log.debug("Request to partially update ApplicationType : {}", applicationType);

        return applicationTypeRepository
            .findById(applicationType.getId())
            .map(existingApplicationType -> {
                if (applicationType.getName() != null) {
                    existingApplicationType.setName(applicationType.getName());
                }

                return existingApplicationType;
            })
            .map(applicationTypeRepository::save);
    }

    /**
     * Get all the applicationTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationType> findAll() {
        log.debug("Request to get all ApplicationTypes");
        return applicationTypeRepository.findAll();
    }

    /**
     * Get one applicationType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationType> findOne(Long id) {
        log.debug("Request to get ApplicationType : {}", id);
        return applicationTypeRepository.findById(id);
    }

    /**
     * Delete the applicationType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApplicationType : {}", id);
        applicationTypeRepository.deleteById(id);
    }
}
