package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SensitiveData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SensitiveData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensitiveDataRepository extends JpaRepository<SensitiveData, Long> {}
