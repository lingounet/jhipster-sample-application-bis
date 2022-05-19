package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hosting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hosting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HostingRepository extends JpaRepository<Hosting, Long> {}
