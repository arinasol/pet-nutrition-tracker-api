package com.kaori.petnutritiontracker.pet;

import com.kaori.petnutritiontracker.pet.dto.CreatePetRequest;
import com.kaori.petnutritiontracker.pet.dto.PetResponse;
import com.kaori.petnutritiontracker.pet.mapper.PetMapper;
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
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetResponse createPet(CreatePetRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Pet pet = createPetFromRequest(request, owner);
        Pet savedPet = petRepository.save(pet);

        return petMapper.toResponse(savedPet);
    }

    public List<PetResponse> getAllPets(String ownerEmail) {
        return petRepository.findAllByOwnerEmail(ownerEmail)
                .stream()
                .map(petMapper::toResponse)
                .toList();
    }

    public PetResponse getPetById(
            Long id,
            String ownerEmail
    ) {
        Pet pet = petRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found"
                ));

        return petMapper.toResponse(pet);
    }

    private Pet createPetFromRequest(CreatePetRequest request, User owner) {
        Pet pet = new Pet();

        pet.setName(request.name());
        pet.setBreed(request.breed());
        pet.setBirthDate(request.birthDate());
        pet.setSpecies(request.species());
        pet.setSex(request.sex());
        pet.setDailyFoodTargetGrams(request.dailyFoodTargetGrams());
        pet.setCreatedAt(LocalDateTime.now());
        pet.setOwner(owner);

        return pet;
    }
}