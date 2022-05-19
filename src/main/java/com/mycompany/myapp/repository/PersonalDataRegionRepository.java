package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersonalDataRegion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonalDataRegion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalDataRegionRepository extends JpaRepository<PersonalDataRegion, Long> {}
