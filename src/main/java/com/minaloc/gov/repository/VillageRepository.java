package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Village;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Village entity.
 */
@Repository
public interface VillageRepository extends JpaRepository<Village, Long>, JpaSpecificationExecutor<Village> {
    default Optional<Village> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Village> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Village> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct village from Village village left join fetch village.cell",
        countQuery = "select count(distinct village) from Village village"
    )
    Page<Village> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct village from Village village left join fetch village.cell")
    List<Village> findAllWithToOneRelationships();

    @Query("select village from Village village left join fetch village.cell where village.id =:id")
    Optional<Village> findOneWithToOneRelationships(@Param("id") Long id);
}
