package com.kaori.petnutritiontracker.pet;

import com.kaori.petnutritiontracker.pet.dto.CreatePetRequest;
import com.kaori.petnutritiontracker.pet.dto.PetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse createPet(
            @Valid @RequestBody CreatePetRequest request,
            Authentication authentication
    ) {
        return petService.createPet(
                request,
                authentication.getName()
        );
    }

    @GetMapping
    public List<PetResponse> getAllPets(
            Authentication authentication
    ) {
        return petService.getAllPets(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public PetResponse getPetById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return petService.getPetById(
                id,
                authentication.getName()
        );
    }
}