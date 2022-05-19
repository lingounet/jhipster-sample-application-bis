package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ComplementaryQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComplementaryQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComplementaryQuestionRepository extends JpaRepository<ComplementaryQuestion, Long> {}
