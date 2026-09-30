package com.kaori.petnutritiontracker.weightrecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeightRecordRepository
        extends JpaRepository<WeightRecord, Long> {

    List<WeightRecord> findAllByPetIdAndPetOwnerEmailOrderByMeasuredAtDesc(
            Long petId,
            String ownerEmail
    );

    Optional<WeightRecord> findByIdAndPetOwnerEmail(
            Long id,
            String ownerEmail
    );

    Optional<WeightRecord> findFirstByPetIdAndPetOwnerEmailOrderByMeasuredAtDesc(
            Long petId,
            String ownerEmail
    );
}