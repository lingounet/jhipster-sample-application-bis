package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Availability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Availability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {}
