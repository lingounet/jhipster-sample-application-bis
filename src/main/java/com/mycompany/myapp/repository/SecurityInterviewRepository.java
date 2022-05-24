package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SecurityInterview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SecurityInterview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecurityInterviewRepository extends JpaRepository<SecurityInterview, Long> {}
