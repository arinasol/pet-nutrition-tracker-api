package com.kaori.petnutritiontracker.feedinglog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedingLogRepository extends JpaRepository<FeedingLog, Long> {

    List<FeedingLog> findAllByPetIdAndPetOwnerEmail(
            Long petId,
            String ownerEmail
    );

    Optional<FeedingLog> findByIdAndPetOwnerEmail(
            Long id,
            String ownerEmail
    );

    /// today eaten grams, daily target, remaining grams
    List<FeedingLog> findAllByPetIdAndPetOwnerEmailAndFedAtGreaterThanEqualAndFedAtLessThan(
            Long petId,
            String ownerEmail,
            LocalDateTime start,
            LocalDateTime end
    );
}