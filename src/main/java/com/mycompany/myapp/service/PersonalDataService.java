package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PersonalData;
import com.mycompany.myapp.repository.PersonalDataRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonalData}.
 */
@Service
@Transactional
public class PersonalDataService {

    private final Logger log = LoggerFactory.getLogger(PersonalDataService.class);

    private final PersonalDataRepository personalDataRepository;

    public PersonalDataService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    /**
     * Save a personalData.
     *
     * @param personalData the entity to save.
     * @return the persisted entity.
     */
    public PersonalData save(PersonalData personalData) {
        log.debug("Request to save PersonalData : {}", personalData);
        return personalDataRepository.save(personalData);
    }

    /**
     * Update a personalData.
     *
     * @param personalData the entity to save.
     * @return the persisted entity.
     */
    public PersonalData update(PersonalData personalData) {
        log.debug("Request to save PersonalData : {}", personalData);
        return personalDataRepository.save(personalData);
    }

    /**
     * Partially update a personalData.
     *
     * @param personalData the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonalData> partialUpdate(PersonalData personalData) {
        log.debug("Request to partially update PersonalData : {}", personalData);

        return personalDataRepository
            .findById(personalData.getId())
            .map(existingPersonalData -> {
                if (personalData.getDate() != null) {
                    existingPersonalData.setDate(personalData.getDate());
                }

                return existingPersonalData;
            })
            .map(personalDataRepository::save);
    }

    /**
     * Get all the personalData.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonalData> findAll() {
        log.debug("Request to get all PersonalData");
        return personalDataRepository.findAll();
    }

    /**
     * Get one personalData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonalData> findOne(Long id) {
        log.debug("Request to get PersonalData : {}", id);
        return personalDataRepository.findById(id);
    }

    /**
     * Delete the personalData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalData : {}", id);
        personalDataRepository.deleteById(id);
    }
}
