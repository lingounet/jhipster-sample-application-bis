package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Aml;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Aml entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmlRepository extends JpaRepository<Aml, Long> {}
