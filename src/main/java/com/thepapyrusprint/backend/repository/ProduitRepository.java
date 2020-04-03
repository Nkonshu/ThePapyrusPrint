package com.thepapyrusprint.backend.repository;

import com.thepapyrusprint.backend.domain.Produit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Produit entity.
 */
@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long>, JpaSpecificationExecutor<Produit> {

    @Query(value = "select distinct produit from Produit produit left join fetch produit.notations left join fetch produit.images",
        countQuery = "select count(distinct produit) from Produit produit")
    Page<Produit> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct produit from Produit produit left join fetch produit.notations left join fetch produit.images")
    List<Produit> findAllWithEagerRelationships();

    @Query("select produit from Produit produit left join fetch produit.notations left join fetch produit.images where produit.id =:id")
    Optional<Produit> findOneWithEagerRelationships(@Param("id") Long id);
}
