package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersonalDataType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonalDataType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalDataTypeRepository extends JpaRepository<PersonalDataType, Long> {}
