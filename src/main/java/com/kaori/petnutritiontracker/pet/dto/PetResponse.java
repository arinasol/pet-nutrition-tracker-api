package com.kaori.petnutritiontracker.pet.dto;

import com.kaori.petnutritiontracker.pet.Sex;
import com.kaori.petnutritiontracker.pet.Species;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PetResponse(
        Long id,
        String name,
        String breed,
        LocalDate birthDate,
        Species species,
        Sex sex,
        BigDecimal dailyFoodTargetGrams,
        LocalDateTime createdAt
) {
}