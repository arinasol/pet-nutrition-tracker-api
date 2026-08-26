package com.kaori.petnutritiontracker.food;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findAllByOwnerEmail(String email);

    Optional<Food> findByIdAndOwnerEmail(Long id, String email);
}