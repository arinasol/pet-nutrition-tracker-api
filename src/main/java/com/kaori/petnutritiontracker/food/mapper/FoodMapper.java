package com.kaori.petnutritiontracker.food.mapper;

import com.kaori.petnutritiontracker.food.Food;
import com.kaori.petnutritiontracker.food.dto.FoodResponse;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public FoodResponse toResponse(Food food) {
        return new FoodResponse(
                food.getId(),
                food.getName(),
                food.getBrand(),
                food.getFoodType(),
                food.getCreatedAt()
        );
    }

}
