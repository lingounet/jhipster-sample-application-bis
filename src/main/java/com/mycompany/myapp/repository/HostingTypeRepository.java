package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HostingType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HostingType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HostingTypeRepository extends JpaRepository<HostingType, Long> {}
