package com.kaori.petnutritiontracker;

import com.kaori.petnutritiontracker.pet.Pet;
import com.kaori.petnutritiontracker.pet.PetRepository;
import com.kaori.petnutritiontracker.pet.PetService;
import com.kaori.petnutritiontracker.pet.dto.PetResponse;
import com.kaori.petnutritiontracker.pet.mapper.PetMapper;
import com.kaori.petnutritiontracker.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetService petService;

    @Test
    void shouldThrowNotFoundWhenPetDoesNotExist() {

        // Arrange
        Long petId = 999999L;
        String ownerEmail = "test@example.com";

        when(petRepository.findByIdAndOwnerEmail(petId, ownerEmail))
                .thenReturn(Optional.empty());

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> petService.getPetById(petId, ownerEmail)
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Pet not found", exception.getReason());

        verify(petRepository).findByIdAndOwnerEmail(petId, ownerEmail);
        verifyNoInteractions(petMapper);
    }

    @Test
    void shouldReturnPetWhenPetExists() {
        // Arrange
        Long petId = 1L;
        String ownerEmail = "test@example.com";

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Barsik");

        PetResponse expectedResponse = mock(PetResponse.class);

        when(petRepository.findByIdAndOwnerEmail(petId, ownerEmail))
                .thenReturn(Optional.of(pet));

        when(petMapper.toResponse(pet))
                .thenReturn(expectedResponse);

        // Act
        PetResponse actualResponse =
                petService.getPetById(petId, ownerEmail);

        // Assert
        assertSame(expectedResponse, actualResponse);

        verify(petRepository)
                .findByIdAndOwnerEmail(petId, ownerEmail);

        verify(petMapper).toResponse(pet);
    }

}