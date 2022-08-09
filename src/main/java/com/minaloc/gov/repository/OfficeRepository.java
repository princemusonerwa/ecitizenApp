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
public interface OfficeRepository extends JpaRepository<Office, Long> {
    default Optional<Office> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Office> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Office> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct office from Office office left join fetch office.user left join fetch office.parent",
        countQuery = "select count(distinct office) from Office office"
    )
    Page<Office> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct office from Office office left join fetch office.user left join fetch office.parent")
    List<Office> findAllWithToOneRelationships();

    @Query("select office from Office office left join fetch office.user left join fetch office.parent where office.id =:id")
    Optional<Office> findOneWithToOneRelationships(@Param("id") Long id);
}
