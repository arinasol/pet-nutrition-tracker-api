package com.kaori.petnutritiontracker.pet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOwnerEmail(String email);

    Optional<Pet> findByIdAndOwnerEmail(Long id, String email);
}