package com.thepapyrusprint.backend.repository;

import com.thepapyrusprint.backend.domain.Facture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Facture entity.
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long>, JpaSpecificationExecutor<Facture> {

    @Query(value = "select distinct facture from Facture facture left join fetch facture.produits",
        countQuery = "select count(distinct facture) from Facture facture")
    Page<Facture> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct facture from Facture facture left join fetch facture.produits")
    List<Facture> findAllWithEagerRelationships();

    @Query("select facture from Facture facture left join fetch facture.produits where facture.id =:id")
    Optional<Facture> findOneWithEagerRelationships(@Param("id") Long id);
}
