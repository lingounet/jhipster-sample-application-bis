package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Icrf;
import com.mycompany.myapp.repository.IcrfRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Icrf}.
 */
@Service
@Transactional
public class IcrfService {

    private final Logger log = LoggerFactory.getLogger(IcrfService.class);

    private final IcrfRepository icrfRepository;

    public IcrfService(IcrfRepository icrfRepository) {
        this.icrfRepository = icrfRepository;
    }

    /**
     * Save a icrf.
     *
     * @param icrf the entity to save.
     * @return the persisted entity.
     */
    public Icrf save(Icrf icrf) {
        log.debug("Request to save Icrf : {}", icrf);
        return icrfRepository.save(icrf);
    }

    /**
     * Update a icrf.
     *
     * @param icrf the entity to save.
     * @return the persisted entity.
     */
    public Icrf update(Icrf icrf) {
        log.debug("Request to save Icrf : {}", icrf);
        return icrfRepository.save(icrf);
    }

    /**
     * Partially update a icrf.
     *
     * @param icrf the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Icrf> partialUpdate(Icrf icrf) {
        log.debug("Request to partially update Icrf : {}", icrf);

        return icrfRepository
            .findById(icrf.getId())
            .map(existingIcrf -> {
                if (icrf.getDate() != null) {
                    existingIcrf.setDate(icrf.getDate());
                }

                return existingIcrf;
            })
            .map(icrfRepository::save);
    }

    /**
     * Get all the icrfs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Icrf> findAll() {
        log.debug("Request to get all Icrfs");
        return icrfRepository.findAll();
    }

    /**
     * Get one icrf by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Icrf> findOne(Long id) {
        log.debug("Request to get Icrf : {}", id);
        return icrfRepository.findById(id);
    }

    /**
     * Delete the icrf by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Icrf : {}", id);
        icrfRepository.deleteById(id);
    }
}
