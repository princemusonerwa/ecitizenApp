package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Complain;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Complain entity.
 */
@Repository
public interface ComplainRepository
    extends ComplainRepositoryWithBagRelationships, JpaRepository<Complain, Long>, JpaSpecificationExecutor<Complain> {
    @Query("select complain from Complain complain where complain.user.login = ?#{principal.username}")
    List<Complain> findByUserIsCurrentUser();

    default Optional<Complain> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Complain> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Complain> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct complain from Complain complain left join fetch complain.user",
        countQuery = "select count(distinct complain) from Complain complain"
    )
    Page<Complain> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct complain from Complain complain left join fetch complain.user")
    List<Complain> findAllWithToOneRelationships();

    @Query("select complain from Complain complain left join fetch complain.user where complain.id =:id")
    Optional<Complain> findOneWithToOneRelationships(@Param("id") Long id);
}
