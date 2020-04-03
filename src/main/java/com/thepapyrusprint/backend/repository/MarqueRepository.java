package com.thepapyrusprint.backend.repository;

import com.thepapyrusprint.backend.domain.Marque;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Marque entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarqueRepository extends JpaRepository<Marque, Long>, JpaSpecificationExecutor<Marque> {
}
