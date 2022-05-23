package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SensitiveData;
import com.mycompany.myapp.repository.SensitiveDataRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SensitiveData}.
 */
@Service
@Transactional
public class SensitiveDataService {

    private final Logger log = LoggerFactory.getLogger(SensitiveDataService.class);

    private final SensitiveDataRepository sensitiveDataRepository;

    public SensitiveDataService(SensitiveDataRepository sensitiveDataRepository) {
        this.sensitiveDataRepository = sensitiveDataRepository;
    }

    /**
     * Save a sensitiveData.
     *
     * @param sensitiveData the entity to save.
     * @return the persisted entity.
     */
    public SensitiveData save(SensitiveData sensitiveData) {
        log.debug("Request to save SensitiveData : {}", sensitiveData);
        return sensitiveDataRepository.save(sensitiveData);
    }

    /**
     * Update a sensitiveData.
     *
     * @param sensitiveData the entity to save.
     * @return the persisted entity.
     */
    public SensitiveData update(SensitiveData sensitiveData) {
        log.debug("Request to save SensitiveData : {}", sensitiveData);
        return sensitiveDataRepository.save(sensitiveData);
    }

    /**
     * Partially update a sensitiveData.
     *
     * @param sensitiveData the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SensitiveData> partialUpdate(SensitiveData sensitiveData) {
        log.debug("Request to partially update SensitiveData : {}", sensitiveData);

        return sensitiveDataRepository
            .findById(sensitiveData.getId())
            .map(existingSensitiveData -> {
                if (sensitiveData.getDate() != null) {
                    existingSensitiveData.setDate(sensitiveData.getDate());
                }

                return existingSensitiveData;
            })
            .map(sensitiveDataRepository::save);
    }

    /**
     * Get all the sensitiveData.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SensitiveData> findAll() {
        log.debug("Request to get all SensitiveData");
        return sensitiveDataRepository.findAll();
    }

    /**
     * Get one sensitiveData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SensitiveData> findOne(Long id) {
        log.debug("Request to get SensitiveData : {}", id);
        return sensitiveDataRepository.findById(id);
    }

    /**
     * Delete the sensitiveData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SensitiveData : {}", id);
        sensitiveDataRepository.deleteById(id);
    }
}
