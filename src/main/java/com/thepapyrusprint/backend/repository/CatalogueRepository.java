package com.thepapyrusprint.backend.repository;

import com.thepapyrusprint.backend.domain.Catalogue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Catalogue entity.
 */
@Repository
public interface CatalogueRepository extends JpaRepository<Catalogue, Long>, JpaSpecificationExecutor<Catalogue> {

    @Query(value = "select distinct catalogue from Catalogue catalogue left join fetch catalogue.produits left join fetch catalogue.fournisseurs",
        countQuery = "select count(distinct catalogue) from Catalogue catalogue")
    Page<Catalogue> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct catalogue from Catalogue catalogue left join fetch catalogue.produits left join fetch catalogue.fournisseurs")
    List<Catalogue> findAllWithEagerRelationships();

    @Query("select catalogue from Catalogue catalogue left join fetch catalogue.produits left join fetch catalogue.fournisseurs where catalogue.id =:id")
    Optional<Catalogue> findOneWithEagerRelationships(@Param("id") Long id);
}
