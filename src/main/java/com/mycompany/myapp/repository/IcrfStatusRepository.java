package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.IcrfStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IcrfStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IcrfStatusRepository extends JpaRepository<IcrfStatus, Long> {}
