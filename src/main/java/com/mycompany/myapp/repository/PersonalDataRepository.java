package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersonalData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonalData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {}
