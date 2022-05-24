package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Icrf;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Icrf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IcrfRepository extends JpaRepository<Icrf, Long> {}
