package com.kaori.petnutritiontracker.pet.dto;

import com.kaori.petnutritiontracker.pet.Sex;
import com.kaori.petnutritiontracker.pet.Species;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePetRequest(

        @NotBlank
        String name,

        String breed,

        LocalDate birthDate,

        @NotNull
        Species species,

        @NotNull
        Sex sex,

        @NotNull
        @Positive
        BigDecimal dailyFoodTargetGrams
) {
}