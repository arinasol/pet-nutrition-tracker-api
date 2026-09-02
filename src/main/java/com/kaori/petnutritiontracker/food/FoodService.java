package com.kaori.petnutritiontracker.food;


import com.kaori.petnutritiontracker.food.dto.CreateFoodRequest;
import com.kaori.petnutritiontracker.food.dto.FoodResponse;
import com.kaori.petnutritiontracker.food.dto.UpdateFoodRequest;
import com.kaori.petnutritiontracker.food.mapper.FoodMapper;
import com.kaori.petnutritiontracker.user.User;
import com.kaori.petnutritiontracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final FoodMapper foodMapper;

    public List<FoodResponse> getAllFoods(String ownerEmail){
        return foodRepository.findAllByOwnerEmail(ownerEmail)
                .stream()
                .map(foodMapper::toResponse)
                .toList();

    }

    public FoodResponse getFoodById(Long id, String ownerEmail) {
        Food food = foodRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Food not found"
                ));
        return foodMapper.toResponse(food);
    }

    public FoodResponse createFood(
            CreateFoodRequest request,
            String ownerEmail
    ) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Food food = createFoodFromRequest(request, owner);

        Food savedFood = foodRepository.save(food);

        return foodMapper.toResponse(savedFood);
    }
    public FoodResponse updateFood(
            Long id,
            UpdateFoodRequest request,
            String ownerEmail
    ) {
        Food food = foodRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Food not found"
                ));

        updateFoodFromRequest(food, request);

        Food updatedFood = foodRepository.save(food);

        return foodMapper.toResponse(updatedFood);
    }

    public void deleteFood(Long id, String ownerEmail) {
        Food food = foodRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Food not found"
                ));

        foodRepository.delete(food);
    }

    private Food createFoodFromRequest(
            CreateFoodRequest request,
            User owner
    ) {
        Food food = new Food();

        food.setName(request.name());
        food.setBrand(request.brand());
        food.setFoodType(request.foodType());
        food.setCreatedAt(LocalDateTime.now());
        food.setOwner(owner);

        return food;
    }

    private void updateFoodFromRequest(
            Food food,
            UpdateFoodRequest request
    ) {
        food.setName(request.name());
        food.setBrand(request.brand());
        food.setFoodType(request.foodType());
    }
}