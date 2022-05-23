package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SensitiveDataType;
import com.mycompany.myapp.repository.SensitiveDataTypeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SensitiveDataType}.
 */
@Service
@Transactional
public class SensitiveDataTypeService {

    private final Logger log = LoggerFactory.getLogger(SensitiveDataTypeService.class);

    private final SensitiveDataTypeRepository sensitiveDataTypeRepository;

    public SensitiveDataTypeService(SensitiveDataTypeRepository sensitiveDataTypeRepository) {
        this.sensitiveDataTypeRepository = sensitiveDataTypeRepository;
    }

    /**
     * Save a sensitiveDataType.
     *
     * @param sensitiveDataType the entity to save.
     * @return the persisted entity.
     */
    public SensitiveDataType save(SensitiveDataType sensitiveDataType) {
        log.debug("Request to save SensitiveDataType : {}", sensitiveDataType);
        return sensitiveDataTypeRepository.save(sensitiveDataType);
    }

    /**
     * Update a sensitiveDataType.
     *
     * @param sensitiveDataType the entity to save.
     * @return the persisted entity.
     */
    public SensitiveDataType update(SensitiveDataType sensitiveDataType) {
        log.debug("Request to save SensitiveDataType : {}", sensitiveDataType);
        return sensitiveDataTypeRepository.save(sensitiveDataType);
    }

    /**
     * Partially update a sensitiveDataType.
     *
     * @param sensitiveDataType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SensitiveDataType> partialUpdate(SensitiveDataType sensitiveDataType) {
        log.debug("Request to partially update SensitiveDataType : {}", sensitiveDataType);

        return sensitiveDataTypeRepository
            .findById(sensitiveDataType.getId())
            .map(existingSensitiveDataType -> {
                if (sensitiveDataType.getName() != null) {
                    existingSensitiveDataType.setName(sensitiveDataType.getName());
                }

                return existingSensitiveDataType;
            })
            .map(sensitiveDataTypeRepository::save);
    }

    /**
     * Get all the sensitiveDataTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SensitiveDataType> findAll() {
        log.debug("Request to get all SensitiveDataTypes");
        return sensitiveDataTypeRepository.findAll();
    }

    /**
     * Get one sensitiveDataType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SensitiveDataType> findOne(Long id) {
        log.debug("Request to get SensitiveDataType : {}", id);
        return sensitiveDataTypeRepository.findById(id);
    }

    /**
     * Delete the sensitiveDataType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SensitiveDataType : {}", id);
        sensitiveDataTypeRepository.deleteById(id);
    }
}
