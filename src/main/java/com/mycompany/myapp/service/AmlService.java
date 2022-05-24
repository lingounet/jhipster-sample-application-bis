package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Aml;
import com.mycompany.myapp.repository.AmlRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Aml}.
 */
@Service
@Transactional
public class AmlService {

    private final Logger log = LoggerFactory.getLogger(AmlService.class);

    private final AmlRepository amlRepository;

    public AmlService(AmlRepository amlRepository) {
        this.amlRepository = amlRepository;
    }

    /**
     * Save a aml.
     *
     * @param aml the entity to save.
     * @return the persisted entity.
     */
    public Aml save(Aml aml) {
        log.debug("Request to save Aml : {}", aml);
        return amlRepository.save(aml);
    }

    /**
     * Update a aml.
     *
     * @param aml the entity to save.
     * @return the persisted entity.
     */
    public Aml update(Aml aml) {
        log.debug("Request to save Aml : {}", aml);
        return amlRepository.save(aml);
    }

    /**
     * Partially update a aml.
     *
     * @param aml the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Aml> partialUpdate(Aml aml) {
        log.debug("Request to partially update Aml : {}", aml);

        return amlRepository
            .findById(aml.getId())
            .map(existingAml -> {
                if (aml.getName() != null) {
                    existingAml.setName(aml.getName());
                }

                return existingAml;
            })
            .map(amlRepository::save);
    }

    /**
     * Get all the amls.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Aml> findAll() {
        log.debug("Request to get all Amls");
        return amlRepository.findAll();
    }

    /**
     * Get one aml by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Aml> findOne(Long id) {
        log.debug("Request to get Aml : {}", id);
        return amlRepository.findById(id);
    }

    /**
     * Delete the aml by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Aml : {}", id);
        amlRepository.deleteById(id);
    }
}
