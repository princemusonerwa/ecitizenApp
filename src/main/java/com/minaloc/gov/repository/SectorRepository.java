package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Sector;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sector entity.
 */
@Repository
public interface SectorRepository extends JpaRepository<Sector, Long>, JpaSpecificationExecutor<Sector> {
    default Optional<Sector> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Sector> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Sector> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct sector from Sector sector left join fetch sector.district",
        countQuery = "select count(distinct sector) from Sector sector"
    )
    Page<Sector> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct sector from Sector sector left join fetch sector.district")
    List<Sector> findAllWithToOneRelationships();

    @Query("select sector from Sector sector left join fetch sector.district where sector.id =:id")
    Optional<Sector> findOneWithToOneRelationships(@Param("id") Long id);
}
