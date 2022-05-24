package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HostingPlatform;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HostingPlatform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HostingPlatformRepository extends JpaRepository<HostingPlatform, Long> {}
