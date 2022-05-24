package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.IcrfAssessment;
import com.mycompany.myapp.repository.IcrfAssessmentRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IcrfAssessment}.
 */
@Service
@Transactional
public class IcrfAssessmentService {

    private final Logger log = LoggerFactory.getLogger(IcrfAssessmentService.class);

    private final IcrfAssessmentRepository icrfAssessmentRepository;

    public IcrfAssessmentService(IcrfAssessmentRepository icrfAssessmentRepository) {
        this.icrfAssessmentRepository = icrfAssessmentRepository;
    }

    /**
     * Save a icrfAssessment.
     *
     * @param icrfAssessment the entity to save.
     * @return the persisted entity.
     */
    public IcrfAssessment save(IcrfAssessment icrfAssessment) {
        log.debug("Request to save IcrfAssessment : {}", icrfAssessment);
        return icrfAssessmentRepository.save(icrfAssessment);
    }

    /**
     * Update a icrfAssessment.
     *
     * @param icrfAssessment the entity to save.
     * @return the persisted entity.
     */
    public IcrfAssessment update(IcrfAssessment icrfAssessment) {
        log.debug("Request to save IcrfAssessment : {}", icrfAssessment);
        return icrfAssessmentRepository.save(icrfAssessment);
    }

    /**
     * Partially update a icrfAssessment.
     *
     * @param icrfAssessment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IcrfAssessment> partialUpdate(IcrfAssessment icrfAssessment) {
        log.debug("Request to partially update IcrfAssessment : {}", icrfAssessment);

        return icrfAssessmentRepository
            .findById(icrfAssessment.getId())
            .map(existingIcrfAssessment -> {
                if (icrfAssessment.getCode() != null) {
                    existingIcrfAssessment.setCode(icrfAssessment.getCode());
                }
                if (icrfAssessment.getDescription() != null) {
                    existingIcrfAssessment.setDescription(icrfAssessment.getDescription());
                }
                if (icrfAssessment.getStatus() != null) {
                    existingIcrfAssessment.setStatus(icrfAssessment.getStatus());
                }

                return existingIcrfAssessment;
            })
            .map(icrfAssessmentRepository::save);
    }

    /**
     * Get all the icrfAssessments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IcrfAssessment> findAll() {
        log.debug("Request to get all IcrfAssessments");
        return icrfAssessmentRepository.findAll();
    }

    /**
     * Get one icrfAssessment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IcrfAssessment> findOne(Long id) {
        log.debug("Request to get IcrfAssessment : {}", id);
        return icrfAssessmentRepository.findById(id);
    }

    /**
     * Delete the icrfAssessment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IcrfAssessment : {}", id);
        icrfAssessmentRepository.deleteById(id);
    }
}
