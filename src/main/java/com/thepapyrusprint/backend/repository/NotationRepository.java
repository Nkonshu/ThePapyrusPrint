package com.thepapyrusprint.backend.repository;

import com.thepapyrusprint.backend.domain.Notation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Notation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotationRepository extends JpaRepository<Notation, Long> {
}
