package com.minaloc.gov.repository;

import com.minaloc.gov.domain.Complain;
import com.minaloc.gov.domain.Umuturage;
import com.minaloc.gov.service.dto.ComplainCategoryDTO;
import com.minaloc.gov.service.dto.ComplainPriorityCountDTO;
import com.minaloc.gov.service.dto.ComplainProvinceCountDTO;
import com.minaloc.gov.service.dto.ComplainStatusCountDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Query(
        "select c.icyakozwe, c.icyakorwa, c.umwanzuro, c.ikibazo, u.indangamuntu from Complain c " +
        "inner join Umuturage u on c.umuturage.indangamuntu = u.indangamuntu" +
        " inner join Village v on v.id = u.village.id inner join Cell cel on cel.id = v.cell.id inner join Sector s on s.id = cel.sector.id" +
        " inner join District d on d.id=s.district.id inner join Province p on p.name=?2"
    )
    Page<Complain> findAllComplainsByProvince(Pageable page, String officeName);

    @Query(
        "select c.icyakozwe, c.icyakorwa, c.umwanzuro, c.ikibazo, u.indangamuntu from Complain c " +
        "inner join Umuturage u on c.umuturage.indangamuntu = u.indangamuntu" +
        " inner join Village v on v.id = u.village.id " +
        "inner join Cell cel on cel.id = v.cell.id " +
        "inner join Sector s on s.id = cel.sector.id inner join District d on d.name=?2"
    )
    Page<Complain> findAllComplainsByDistrict(Pageable page, String officeName);

    @Query(
        "select c.icyakozwe, c.icyakorwa, c.umwanzuro, c.ikibazo, u.indangamuntu from Complain c " +
        "inner join Umuturage u on c.umuturage.indangamuntu = u.indangamuntu" +
        " inner join Village v on v.id = u.village.id inner join Cell cel on cel.id = v.cell.id " +
        "inner join Sector s on s.name=?2"
    )
    Page<Complain> findAllComplainsBySector(Pageable page, String officeName);

    @Query(
        "select c.icyakozwe, c.icyakorwa, c.umwanzuro, c.ikibazo, u.indangamuntu from Complain c " +
        "inner join Umuturage u on c.umuturage.indangamuntu = u.indangamuntu" +
        " inner join Village v on v.id = u.village.id inner join Cell cel on cel.name=?2"
    )
    Page<Complain> findAllComplainsByCell(Pageable page, String officeName);

    @Query("select complain from Complain complain where complain.id =:id")
    Complain getById(@Param("id") Long id);

    @Query("select complain.umuturage from Complain complain where complain.id = :id")
    Umuturage findUmuturageComplain(@Param("id") Long id);

    @Query("SELECT new com.minaloc.gov.service.dto.ComplainStatusCountDTO(c.status, COUNT(c.status)) FROM Complain AS c GROUP BY c.status")
    List<ComplainStatusCountDTO> getCountByStatus();

    @Query(
        "SELECT new com.minaloc.gov.service.dto.ComplainPriorityCountDTO(c.priority, COUNT(c.priority)) FROM Complain AS c GROUP BY c.priority"
    )
    List<ComplainPriorityCountDTO> getCountByPriority();

    @Query(
        "select new com.minaloc.gov.service.dto.ComplainCategoryDTO(cy.name, COUNT(*)) from Complain c LEFT JOIN Category cy on c.category=cy.id group by cy.name"
    )
    List<ComplainCategoryDTO> getComplainBasedOnCategory();

    @Query(
        "select new com.minaloc.gov.service.dto.ComplainProvinceCountDTO(p.name, COUNT(p.name)) from Complain c inner join Umuturage u on c.umuturage.id = u.id inner join Village v on v.id = u.village.id inner join Cell cel on cel.id = v.cell.id inner join Sector s on s.id = cel.sector.id inner join District d on d.id=s.district.id inner join Province p on p.id = d.province.id group by p.name"
    )
    List<ComplainProvinceCountDTO> getComplainBasedOnProvince();

    @Query("select complain from Complain complain where complain.createdAt >= :from and complain.createdAt <= :to")
    Page<Complain> findAll(Pageable pageable, @Param("from") Instant from, @Param("to") Instant to);

    @Query(
        "select complain from Complain complain where lower(complain.ikibazo) LIKE lower(concat('%', ?1,'%')) OR lower(complain.icyakozwe) LIKE lower(concat('%', ?1,'%'))" +
        "OR lower(complain.icyakorwa) LIKE lower(concat('%', ?1,'%')) OR lower(complain.umwanzuro) LIKE lower(concat('%', ?1,'%')) OR lower(complain.status) LIKE lower(concat('%', ?1,'%')) OR lower(complain.priority) LIKE lower(concat('%', ?1,'%'))" +
        "OR lower(complain.category.name) LIKE lower(concat('%', ?1,'%')) OR lower(complain.umuturage.indangamuntu) LIKE lower(concat('%', ?1,'%')) OR lower(complain.umuturage.amazina) LIKE lower(concat('%', ?1,'%'))"
    )
    Page<Complain> findAll(Pageable pageable, String keyword);
}
