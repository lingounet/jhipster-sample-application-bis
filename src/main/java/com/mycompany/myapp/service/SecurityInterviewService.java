package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SecurityInterview;
import com.mycompany.myapp.repository.SecurityInterviewRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SecurityInterview}.
 */
@Service
@Transactional
public class SecurityInterviewService {

    private final Logger log = LoggerFactory.getLogger(SecurityInterviewService.class);

    private final SecurityInterviewRepository securityInterviewRepository;

    public SecurityInterviewService(SecurityInterviewRepository securityInterviewRepository) {
        this.securityInterviewRepository = securityInterviewRepository;
    }

    /**
     * Save a securityInterview.
     *
     * @param securityInterview the entity to save.
     * @return the persisted entity.
     */
    public SecurityInterview save(SecurityInterview securityInterview) {
        log.debug("Request to save SecurityInterview : {}", securityInterview);
        return securityInterviewRepository.save(securityInterview);
    }

    /**
     * Update a securityInterview.
     *
     * @param securityInterview the entity to save.
     * @return the persisted entity.
     */
    public SecurityInterview update(SecurityInterview securityInterview) {
        log.debug("Request to save SecurityInterview : {}", securityInterview);
        return securityInterviewRepository.save(securityInterview);
    }

    /**
     * Partially update a securityInterview.
     *
     * @param securityInterview the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SecurityInterview> partialUpdate(SecurityInterview securityInterview) {
        log.debug("Request to partially update SecurityInterview : {}", securityInterview);

        return securityInterviewRepository
            .findById(securityInterview.getId())
            .map(existingSecurityInterview -> {
                if (securityInterview.getApplicationName() != null) {
                    existingSecurityInterview.setApplicationName(securityInterview.getApplicationName());
                }
                if (securityInterview.getSo() != null) {
                    existingSecurityInterview.setSo(securityInterview.getSo());
                }
                if (securityInterview.getProcess() != null) {
                    existingSecurityInterview.setProcess(securityInterview.getProcess());
                }

                return existingSecurityInterview;
            })
            .map(securityInterviewRepository::save);
    }

    /**
     * Get all the securityInterviews.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SecurityInterview> findAll() {
        log.debug("Request to get all SecurityInterviews");
        return securityInterviewRepository.findAll();
    }

    /**
     *  Get all the securityInterviews where Psat is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SecurityInterview> findAllWherePsatIsNull() {
        log.debug("Request to get all securityInterviews where Psat is null");
        return StreamSupport
            .stream(securityInterviewRepository.findAll().spliterator(), false)
            .filter(securityInterview -> securityInterview.getPsat() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one securityInterview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SecurityInterview> findOne(Long id) {
        log.debug("Request to get SecurityInterview : {}", id);
        return securityInterviewRepository.findById(id);
    }

    /**
     * Delete the securityInterview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityInterview : {}", id);
        securityInterviewRepository.deleteById(id);
    }
}
