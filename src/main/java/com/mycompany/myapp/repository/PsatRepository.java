package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Psat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Psat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PsatRepository extends JpaRepository<Psat, Long> {}
