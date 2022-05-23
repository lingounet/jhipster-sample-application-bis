package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ApplicationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationTypeRepository extends JpaRepository<ApplicationType, Long> {}
