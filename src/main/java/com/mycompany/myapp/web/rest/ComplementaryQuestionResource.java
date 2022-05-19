package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ComplementaryQuestion;
import com.mycompany.myapp.repository.ComplementaryQuestionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ComplementaryQuestion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ComplementaryQuestionResource {

    private final Logger log = LoggerFactory.getLogger(ComplementaryQuestionResource.class);

    private static final String ENTITY_NAME = "complementaryQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComplementaryQuestionRepository complementaryQuestionRepository;

    public ComplementaryQuestionResource(ComplementaryQuestionRepository complementaryQuestionRepository) {
        this.complementaryQuestionRepository = complementaryQuestionRepository;
    }

    /**
     * {@code POST  /complementary-questions} : Create a new complementaryQuestion.
     *
     * @param complementaryQuestion the complementaryQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complementaryQuestion, or with status {@code 400 (Bad Request)} if the complementaryQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/complementary-questions")
    public ResponseEntity<ComplementaryQuestion> createComplementaryQuestion(@RequestBody ComplementaryQuestion complementaryQuestion)
        throws URISyntaxException {
        log.debug("REST request to save ComplementaryQuestion : {}", complementaryQuestion);
        if (complementaryQuestion.getId() != null) {
            throw new BadRequestAlertException("A new complementaryQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComplementaryQuestion result = complementaryQuestionRepository.save(complementaryQuestion);
        return ResponseEntity
            .created(new URI("/api/complementary-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /complementary-questions/:id} : Updates an existing complementaryQuestion.
     *
     * @param id the id of the complementaryQuestion to save.
     * @param complementaryQuestion the complementaryQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complementaryQuestion,
     * or with status {@code 400 (Bad Request)} if the complementaryQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complementaryQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/complementary-questions/{id}")
    public ResponseEntity<ComplementaryQuestion> updateComplementaryQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComplementaryQuestion complementaryQuestion
    ) throws URISyntaxException {
        log.debug("REST request to update ComplementaryQuestion : {}, {}", id, complementaryQuestion);
        if (complementaryQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complementaryQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complementaryQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComplementaryQuestion result = complementaryQuestionRepository.save(complementaryQuestion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, complementaryQuestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /complementary-questions/:id} : Partial updates given fields of an existing complementaryQuestion, field will ignore if it is null
     *
     * @param id the id of the complementaryQuestion to save.
     * @param complementaryQuestion the complementaryQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complementaryQuestion,
     * or with status {@code 400 (Bad Request)} if the complementaryQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the complementaryQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the complementaryQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/complementary-questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComplementaryQuestion> partialUpdateComplementaryQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComplementaryQuestion complementaryQuestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComplementaryQuestion partially : {}, {}", id, complementaryQuestion);
        if (complementaryQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complementaryQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complementaryQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComplementaryQuestion> result = complementaryQuestionRepository
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

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, complementaryQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /complementary-questions} : get all the complementaryQuestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of complementaryQuestions in body.
     */
    @GetMapping("/complementary-questions")
    public List<ComplementaryQuestion> getAllComplementaryQuestions() {
        log.debug("REST request to get all ComplementaryQuestions");
        return complementaryQuestionRepository.findAll();
    }

    /**
     * {@code GET  /complementary-questions/:id} : get the "id" complementaryQuestion.
     *
     * @param id the id of the complementaryQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the complementaryQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/complementary-questions/{id}")
    public ResponseEntity<ComplementaryQuestion> getComplementaryQuestion(@PathVariable Long id) {
        log.debug("REST request to get ComplementaryQuestion : {}", id);
        Optional<ComplementaryQuestion> complementaryQuestion = complementaryQuestionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(complementaryQuestion);
    }

    /**
     * {@code DELETE  /complementary-questions/:id} : delete the "id" complementaryQuestion.
     *
     * @param id the id of the complementaryQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/complementary-questions/{id}")
    public ResponseEntity<Void> deleteComplementaryQuestion(@PathVariable Long id) {
        log.debug("REST request to delete ComplementaryQuestion : {}", id);
        complementaryQuestionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
