package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Umurimo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Umurimo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UmurimoRepository extends JpaRepository<Umurimo, Long>, JpaSpecificationExecutor<Umurimo> {}
