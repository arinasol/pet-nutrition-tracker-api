package com.kaori.petnutritiontracker.food.dto;

import com.kaori.petnutritiontracker.food.FoodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFoodRequest(

        @NotBlank
        String name,

        String brand,

        @NotNull
        FoodType foodType
) {
}