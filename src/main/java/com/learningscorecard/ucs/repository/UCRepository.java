package com.learningscorecard.ucs.repository;

import com.learningscorecard.ucs.model.entity.UC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UCRepository extends JpaRepository<UC, UUID> {

    Optional<UC> findByName(String name);

    Boolean existsByNameAndAcademicYear(String name, String academicYear);
}
