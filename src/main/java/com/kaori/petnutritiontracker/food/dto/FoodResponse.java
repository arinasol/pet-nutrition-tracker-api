package com.kaori.petnutritiontracker.food.dto;

import com.kaori.petnutritiontracker.food.FoodType;

import java.time.LocalDateTime;

public record FoodResponse(
        Long id,
        String name,
        String brand,
        FoodType foodType,
        LocalDateTime createdAt
) {
}