package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PersonalDataType;
import com.mycompany.myapp.repository.PersonalDataTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonalDataType}.
 */
@Service
@Transactional
public class PersonalDataTypeService {

    private final Logger log = LoggerFactory.getLogger(PersonalDataTypeService.class);

    private final PersonalDataTypeRepository personalDataTypeRepository;

    public PersonalDataTypeService(PersonalDataTypeRepository personalDataTypeRepository) {
        this.personalDataTypeRepository = personalDataTypeRepository;
    }

    /**
     * Save a personalDataType.
     *
     * @param personalDataType the entity to save.
     * @return the persisted entity.
     */
    public PersonalDataType save(PersonalDataType personalDataType) {
        log.debug("Request to save PersonalDataType : {}", personalDataType);
        return personalDataTypeRepository.save(personalDataType);
    }

    /**
     * Update a personalDataType.
     *
     * @param personalDataType the entity to save.
     * @return the persisted entity.
     */
    public PersonalDataType update(PersonalDataType personalDataType) {
        log.debug("Request to save PersonalDataType : {}", personalDataType);
        return personalDataTypeRepository.save(personalDataType);
    }

    /**
     * Partially update a personalDataType.
     *
     * @param personalDataType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonalDataType> partialUpdate(PersonalDataType personalDataType) {
        log.debug("Request to partially update PersonalDataType : {}", personalDataType);

        return personalDataTypeRepository
            .findById(personalDataType.getId())
            .map(existingPersonalDataType -> {
                if (personalDataType.getName() != null) {
                    existingPersonalDataType.setName(personalDataType.getName());
                }

                return existingPersonalDataType;
            })
            .map(personalDataTypeRepository::save);
    }

    /**
     * Get all the personalDataTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonalDataType> findAll() {
        log.debug("Request to get all PersonalDataTypes");
        return personalDataTypeRepository.findAll();
    }

    /**
     * Get one personalDataType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonalDataType> findOne(Long id) {
        log.debug("Request to get PersonalDataType : {}", id);
        return personalDataTypeRepository.findById(id);
    }

    /**
     * Delete the personalDataType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalDataType : {}", id);
        personalDataTypeRepository.deleteById(id);
    }
}
