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
}
