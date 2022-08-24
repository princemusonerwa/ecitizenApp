package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Office;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Office entity.
 */
@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {}
