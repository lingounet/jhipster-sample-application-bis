package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ComplementaryQuestion;
import com.mycompany.myapp.repository.ComplementaryQuestionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ComplementaryQuestion}.
 */
@Service
@Transactional
public class ComplementaryQuestionService {

    private final Logger log = LoggerFactory.getLogger(ComplementaryQuestionService.class);

    private final ComplementaryQuestionRepository complementaryQuestionRepository;

    public ComplementaryQuestionService(ComplementaryQuestionRepository complementaryQuestionRepository) {
        this.complementaryQuestionRepository = complementaryQuestionRepository;
    }

    /**
     * Save a complementaryQuestion.
     *
     * @param complementaryQuestion the entity to save.
     * @return the persisted entity.
     */
    public ComplementaryQuestion save(ComplementaryQuestion complementaryQuestion) {
        log.debug("Request to save ComplementaryQuestion : {}", complementaryQuestion);
        return complementaryQuestionRepository.save(complementaryQuestion);
    }

    /**
     * Update a complementaryQuestion.
     *
     * @param complementaryQuestion the entity to save.
     * @return the persisted entity.
     */
    public ComplementaryQuestion update(ComplementaryQuestion complementaryQuestion) {
        log.debug("Request to save ComplementaryQuestion : {}", complementaryQuestion);
        return complementaryQuestionRepository.save(complementaryQuestion);
    }

    /**
     * Partially update a complementaryQuestion.
     *
     * @param complementaryQuestion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComplementaryQuestion> partialUpdate(ComplementaryQuestion complementaryQuestion) {
        log.debug("Request to partially update ComplementaryQuestion : {}", complementaryQuestion);

        return complementaryQuestionRepository
            .findById(complementaryQuestion.getId())
            .map(existingComplementaryQuestion -> {
                if (complementaryQuestion.getInternet() != null) {
                    existingComplementaryQuestion.setInternet(complementaryQuestion.getInternet());
                }
                if (complementaryQuestion.getDevelopment() != null) {
                    existingComplementaryQuestion.setDevelopment(complementaryQuestion.getDevelopment());
                }
                if (complementaryQuestion.getConfiguration() != null) {
                    existingComplementaryQuestion.setConfiguration(complementaryQuestion.getConfiguration());
                }
                if (complementaryQuestion.getCloud() != null) {
                    existingComplementaryQuestion.setCloud(complementaryQuestion.getCloud());
                }
                if (complementaryQuestion.getInternal() != null) {
                    existingComplementaryQuestion.setInternal(complementaryQuestion.getInternal());
                }
                if (complementaryQuestion.getPartner() != null) {
                    existingComplementaryQuestion.setPartner(complementaryQuestion.getPartner());
                }
                if (complementaryQuestion.getUsers() != null) {
                    existingComplementaryQuestion.setUsers(complementaryQuestion.getUsers());
                }

                return existingComplementaryQuestion;
            })
            .map(complementaryQuestionRepository::save);
    }

    /**
     * Get all the complementaryQuestions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ComplementaryQuestion> findAll() {
        log.debug("Request to get all ComplementaryQuestions");
        return complementaryQuestionRepository.findAll();
    }

    /**
     * Get one complementaryQuestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComplementaryQuestion> findOne(Long id) {
        log.debug("Request to get ComplementaryQuestion : {}", id);
        return complementaryQuestionRepository.findById(id);
    }

    /**
     * Delete the complementaryQuestion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ComplementaryQuestion : {}", id);
        complementaryQuestionRepository.deleteById(id);
    }
}
