package com.kaori.petnutritiontracker.pet.mapper;

import com.kaori.petnutritiontracker.pet.Pet;
import com.kaori.petnutritiontracker.pet.dto.PetResponse;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public PetResponse toResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getSpecies(),
                pet.getSex(),
                pet.getDailyFoodTargetGrams(),
                pet.getCreatedAt()
        );
    }
}