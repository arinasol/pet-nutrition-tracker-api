package com.kaori.petnutritiontracker.food;

import com.kaori.petnutritiontracker.food.dto.CreateFoodRequest;
import com.kaori.petnutritiontracker.food.dto.FoodResponse;
import com.kaori.petnutritiontracker.food.dto.UpdateFoodRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodResponse createFood(
            @Valid @RequestBody CreateFoodRequest request,
            Authentication authentication
    ) {
        return foodService.createFood(
                request,
                authentication.getName()
        );
    }

    @GetMapping
    public List<FoodResponse> getAllFoods(
            Authentication authentication
    ) {
        return foodService.getAllFoods(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public FoodResponse getFoodById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return foodService.getFoodById(
                id,
                authentication.getName()
        );
    }

    @PutMapping("/{id}")
    public FoodResponse updateFood(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFoodRequest request,
            Authentication authentication
    ) {
        return foodService.updateFood(
                id,
                request,
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(
            @PathVariable Long id,
            Authentication authentication
    ) {
        foodService.deleteFood(
                id,
                authentication.getName()
        );
    }
}