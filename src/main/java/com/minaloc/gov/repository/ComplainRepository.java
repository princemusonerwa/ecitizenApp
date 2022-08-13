package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.service.dto.ComplainPriorityCount;
import com.minaloc.gov.service.dto.ComplainStatusCount;
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

    @Query("select complain from Complain complain where complain.id =:id")
    Complain getById(@Param("id") Long id);

    @Query("select umuturage from Complain complain where complain.id = :id")
    Umuturage findUmuturageComplain(@Param("id") Long id);

    @Query("SELECT new com.minaloc.gov.service.dto.ComplainStatusCount(c.status, COUNT(c.status)) FROM Complain AS c GROUP BY c.status")
    List<ComplainStatusCount> getCountByStatus();

    @Query(
        "SELECT new com.minaloc.gov.service.dto.ComplainPriorityCount(c.priority, COUNT(c.priority)) FROM Complain AS c GROUP BY c.priority"
    )
    List<ComplainPriorityCount> getCountByPriority();

    @Query("select cy.name, count(*) as count from Complain c LEFT JOIN Category cy on c.category=cy.id group by cy.name")
    List<Object> getComplainBasedOnCategory();
}
