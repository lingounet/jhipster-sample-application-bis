package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SensitiveDataType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SensitiveDataType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensitiveDataTypeRepository extends JpaRepository<SensitiveDataType, Long> {}
