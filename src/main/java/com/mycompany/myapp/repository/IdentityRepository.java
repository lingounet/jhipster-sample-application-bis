package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Identity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Identity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {}
