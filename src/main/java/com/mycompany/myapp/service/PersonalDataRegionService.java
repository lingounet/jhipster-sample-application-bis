package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PersonalDataRegion;
import com.mycompany.myapp.repository.PersonalDataRegionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonalDataRegion}.
 */
@Service
@Transactional
public class PersonalDataRegionService {

    private final Logger log = LoggerFactory.getLogger(PersonalDataRegionService.class);

    private final PersonalDataRegionRepository personalDataRegionRepository;

    public PersonalDataRegionService(PersonalDataRegionRepository personalDataRegionRepository) {
        this.personalDataRegionRepository = personalDataRegionRepository;
    }

    /**
     * Save a personalDataRegion.
     *
     * @param personalDataRegion the entity to save.
     * @return the persisted entity.
     */
    public PersonalDataRegion save(PersonalDataRegion personalDataRegion) {
        log.debug("Request to save PersonalDataRegion : {}", personalDataRegion);
        return personalDataRegionRepository.save(personalDataRegion);
    }

    /**
     * Update a personalDataRegion.
     *
     * @param personalDataRegion the entity to save.
     * @return the persisted entity.
     */
    public PersonalDataRegion update(PersonalDataRegion personalDataRegion) {
        log.debug("Request to save PersonalDataRegion : {}", personalDataRegion);
        return personalDataRegionRepository.save(personalDataRegion);
    }

    /**
     * Partially update a personalDataRegion.
     *
     * @param personalDataRegion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonalDataRegion> partialUpdate(PersonalDataRegion personalDataRegion) {
        log.debug("Request to partially update PersonalDataRegion : {}", personalDataRegion);

        return personalDataRegionRepository
            .findById(personalDataRegion.getId())
            .map(existingPersonalDataRegion -> {
                if (personalDataRegion.getName() != null) {
                    existingPersonalDataRegion.setName(personalDataRegion.getName());
                }

                return existingPersonalDataRegion;
            })
            .map(personalDataRegionRepository::save);
    }

    /**
     * Get all the personalDataRegions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonalDataRegion> findAll() {
        log.debug("Request to get all PersonalDataRegions");
        return personalDataRegionRepository.findAll();
    }

    /**
     * Get one personalDataRegion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonalDataRegion> findOne(Long id) {
        log.debug("Request to get PersonalDataRegion : {}", id);
        return personalDataRegionRepository.findById(id);
    }

    /**
     * Delete the personalDataRegion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalDataRegion : {}", id);
        personalDataRegionRepository.deleteById(id);
    }
}
