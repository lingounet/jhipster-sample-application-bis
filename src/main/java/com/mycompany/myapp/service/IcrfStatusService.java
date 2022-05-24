package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.IcrfStatus;
import com.mycompany.myapp.repository.IcrfStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IcrfStatus}.
 */
@Service
@Transactional
public class IcrfStatusService {

    private final Logger log = LoggerFactory.getLogger(IcrfStatusService.class);

    private final IcrfStatusRepository icrfStatusRepository;

    public IcrfStatusService(IcrfStatusRepository icrfStatusRepository) {
        this.icrfStatusRepository = icrfStatusRepository;
    }

    /**
     * Save a icrfStatus.
     *
     * @param icrfStatus the entity to save.
     * @return the persisted entity.
     */
    public IcrfStatus save(IcrfStatus icrfStatus) {
        log.debug("Request to save IcrfStatus : {}", icrfStatus);
        return icrfStatusRepository.save(icrfStatus);
    }

    /**
     * Update a icrfStatus.
     *
     * @param icrfStatus the entity to save.
     * @return the persisted entity.
     */
    public IcrfStatus update(IcrfStatus icrfStatus) {
        log.debug("Request to save IcrfStatus : {}", icrfStatus);
        return icrfStatusRepository.save(icrfStatus);
    }

    /**
     * Partially update a icrfStatus.
     *
     * @param icrfStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IcrfStatus> partialUpdate(IcrfStatus icrfStatus) {
        log.debug("Request to partially update IcrfStatus : {}", icrfStatus);

        return icrfStatusRepository
            .findById(icrfStatus.getId())
            .map(existingIcrfStatus -> {
                if (icrfStatus.getName() != null) {
                    existingIcrfStatus.setName(icrfStatus.getName());
                }

                return existingIcrfStatus;
            })
            .map(icrfStatusRepository::save);
    }

    /**
     * Get all the icrfStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IcrfStatus> findAll() {
        log.debug("Request to get all IcrfStatuses");
        return icrfStatusRepository.findAll();
    }

    /**
     *  Get all the icrfStatuses where Icrf is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IcrfStatus> findAllWhereIcrfIsNull() {
        log.debug("Request to get all icrfStatuses where Icrf is null");
        return StreamSupport
            .stream(icrfStatusRepository.findAll().spliterator(), false)
            .filter(icrfStatus -> icrfStatus.getIcrf() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one icrfStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IcrfStatus> findOne(Long id) {
        log.debug("Request to get IcrfStatus : {}", id);
        return icrfStatusRepository.findById(id);
    }

    /**
     * Delete the icrfStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IcrfStatus : {}", id);
        icrfStatusRepository.deleteById(id);
    }
}
