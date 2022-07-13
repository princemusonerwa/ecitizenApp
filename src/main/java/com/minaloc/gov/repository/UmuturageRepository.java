package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Umuturage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Umuturage entity.
 */
@Repository
public interface UmuturageRepository extends JpaRepository<Umuturage, Long>, JpaSpecificationExecutor<Umuturage> {
    @Query("select umuturage from Umuturage umuturage where umuturage.user.login = ?#{principal.username}")
    List<Umuturage> findByUserIsCurrentUser();

    default Optional<Umuturage> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Umuturage> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Umuturage> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct umuturage from Umuturage umuturage left join fetch umuturage.user left join fetch umuturage.village",
        countQuery = "select count(distinct umuturage) from Umuturage umuturage"
    )
    Page<Umuturage> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct umuturage from Umuturage umuturage left join fetch umuturage.user left join fetch umuturage.village")
    List<Umuturage> findAllWithToOneRelationships();

    @Query(
        "select umuturage from Umuturage umuturage left join fetch umuturage.user left join fetch umuturage.village where umuturage.id =:id"
    )
    Optional<Umuturage> findOneWithToOneRelationships(@Param("id") Long id);
}
