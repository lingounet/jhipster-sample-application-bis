package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Availability;
import com.mycompany.myapp.repository.AvailabilityRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Availability}.
 */
@Service
@Transactional
public class AvailabilityService {

    private final Logger log = LoggerFactory.getLogger(AvailabilityService.class);

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Save a availability.
     *
     * @param availability the entity to save.
     * @return the persisted entity.
     */
    public Availability save(Availability availability) {
        log.debug("Request to save Availability : {}", availability);
        return availabilityRepository.save(availability);
    }

    /**
     * Update a availability.
     *
     * @param availability the entity to save.
     * @return the persisted entity.
     */
    public Availability update(Availability availability) {
        log.debug("Request to save Availability : {}", availability);
        return availabilityRepository.save(availability);
    }

    /**
     * Partially update a availability.
     *
     * @param availability the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Availability> partialUpdate(Availability availability) {
        log.debug("Request to partially update Availability : {}", availability);

        return availabilityRepository
            .findById(availability.getId())
            .map(existingAvailability -> {
                if (availability.getFinancial() != null) {
                    existingAvailability.setFinancial(availability.getFinancial());
                }
                if (availability.getLegal() != null) {
                    existingAvailability.setLegal(availability.getLegal());
                }
                if (availability.getImage() != null) {
                    existingAvailability.setImage(availability.getImage());
                }
                if (availability.getStrategy() != null) {
                    existingAvailability.setStrategy(availability.getStrategy());
                }
                if (availability.getOperational() != null) {
                    existingAvailability.setOperational(availability.getOperational());
                }
                if (availability.getTraceability() != null) {
                    existingAvailability.setTraceability(availability.getTraceability());
                }
                if (availability.getTraceabilityContentType() != null) {
                    existingAvailability.setTraceabilityContentType(availability.getTraceabilityContentType());
                }
                if (availability.getConfidentiality() != null) {
                    existingAvailability.setConfidentiality(availability.getConfidentiality());
                }
                if (availability.getConfidentialityContentType() != null) {
                    existingAvailability.setConfidentialityContentType(availability.getConfidentialityContentType());
                }
                if (availability.getIntegrity() != null) {
                    existingAvailability.setIntegrity(availability.getIntegrity());
                }
                if (availability.getIntegrityContentType() != null) {
                    existingAvailability.setIntegrityContentType(availability.getIntegrityContentType());
                }
                if (availability.getCritical() != null) {
                    existingAvailability.setCritical(availability.getCritical());
                }

                return existingAvailability;
            })
            .map(availabilityRepository::save);
    }

    /**
     * Get all the availabilities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Availability> findAll() {
        log.debug("Request to get all Availabilities");
        return availabilityRepository.findAll();
    }

    /**
     *  Get all the availabilities where SecurityInterview is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Availability> findAllWhereSecurityInterviewIsNull() {
        log.debug("Request to get all availabilities where SecurityInterview is null");
        return StreamSupport
            .stream(availabilityRepository.findAll().spliterator(), false)
            .filter(availability -> availability.getSecurityInterview() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one availability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Availability> findOne(Long id) {
        log.debug("Request to get Availability : {}", id);
        return availabilityRepository.findById(id);
    }

    /**
     * Delete the availability by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Availability : {}", id);
        availabilityRepository.deleteById(id);
    }
}
