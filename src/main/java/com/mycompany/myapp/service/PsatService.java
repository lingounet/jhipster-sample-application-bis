package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Psat;
import com.mycompany.myapp.repository.PsatRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Psat}.
 */
@Service
@Transactional
public class PsatService {

    private final Logger log = LoggerFactory.getLogger(PsatService.class);

    private final PsatRepository psatRepository;

    public PsatService(PsatRepository psatRepository) {
        this.psatRepository = psatRepository;
    }

    /**
     * Save a psat.
     *
     * @param psat the entity to save.
     * @return the persisted entity.
     */
    public Psat save(Psat psat) {
        log.debug("Request to save Psat : {}", psat);
        return psatRepository.save(psat);
    }

    /**
     * Update a psat.
     *
     * @param psat the entity to save.
     * @return the persisted entity.
     */
    public Psat update(Psat psat) {
        log.debug("Request to save Psat : {}", psat);
        return psatRepository.save(psat);
    }

    /**
     * Partially update a psat.
     *
     * @param psat the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Psat> partialUpdate(Psat psat) {
        log.debug("Request to partially update Psat : {}", psat);

        return psatRepository
            .findById(psat.getId())
            .map(existingPsat -> {
                if (psat.getAmlId() != null) {
                    existingPsat.setAmlId(psat.getAmlId());
                }
                if (psat.getOwner() != null) {
                    existingPsat.setOwner(psat.getOwner());
                }
                if (psat.getStatus() != null) {
                    existingPsat.setStatus(psat.getStatus());
                }

                return existingPsat;
            })
            .map(psatRepository::save);
    }

    /**
     * Get all the psats.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Psat> findAll() {
        log.debug("Request to get all Psats");
        return psatRepository.findAll();
    }

    /**
     * Get one psat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Psat> findOne(Long id) {
        log.debug("Request to get Psat : {}", id);
        return psatRepository.findById(id);
    }

    /**
     * Delete the psat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Psat : {}", id);
        psatRepository.deleteById(id);
    }
}
