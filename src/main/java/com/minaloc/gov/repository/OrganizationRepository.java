package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Organization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Organization entity.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    default Optional<Organization> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Organization> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Organization> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct organization from Organization organization left join fetch organization.user",
        countQuery = "select count(distinct organization) from Organization organization"
    )
    Page<Organization> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct organization from Organization organization left join fetch organization.user")
    List<Organization> findAllWithToOneRelationships();

    @Query("select organization from Organization organization left join fetch organization.user where organization.id =:id")
    Optional<Organization> findOneWithToOneRelationships(@Param("id") Long id);
}
