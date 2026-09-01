package com.kaori.petnutritiontracker.food;


import com.kaori.petnutritiontracker.food.dto.FoodResponse;
import com.kaori.petnutritiontracker.food.mapper.FoodMapper;
import com.kaori.petnutritiontracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

}