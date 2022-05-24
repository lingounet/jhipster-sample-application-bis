package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.IcrfAssessment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IcrfAssessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IcrfAssessmentRepository extends JpaRepository<IcrfAssessment, Long> {}
