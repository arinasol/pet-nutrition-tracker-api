package com.kaori.petnutritiontracker.feedinglog;

import com.kaori.petnutritiontracker.feedinglog.dto.CreateFeedingLogRequest;
import com.kaori.petnutritiontracker.feedinglog.dto.FeedingLogResponse;
import com.kaori.petnutritiontracker.feedinglog.mapper.FeedingLogMapper;
import com.kaori.petnutritiontracker.food.Food;
import com.kaori.petnutritiontracker.food.FoodRepository;
import com.kaori.petnutritiontracker.pet.Pet;
import com.kaori.petnutritiontracker.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedingLogService {

    private final FeedingLogRepository feedingLogRepository;
    private final PetRepository petRepository;
    private final FoodRepository foodRepository;
    private final FeedingLogMapper feedingLogMapper;

    public FeedingLogResponse createFeedingLog(
            CreateFeedingLogRequest request,
            String ownerEmail
    ) {
        Pet pet = petRepository.findByIdAndOwnerEmail(
                        request.petId(),
                        ownerEmail
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found"
                ));

        Food food = foodRepository.findByIdAndOwnerEmail(
                        request.foodId(),
                        ownerEmail
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Food not found"
                ));

        FeedingLog feedingLog = createFromRequest(
                request,
                pet,
                food
        );

        FeedingLog savedLog = feedingLogRepository.save(feedingLog);

        return feedingLogMapper.toResponse(savedLog);
    }

    public List<FeedingLogResponse> getAllForPet(
            Long petId,
            String ownerEmail
    ) {
        return feedingLogRepository
                .findAllByPetIdAndPetOwnerEmail(petId, ownerEmail)
                .stream()
                .map(feedingLogMapper::toResponse)
                .toList();
    }

    public FeedingLogResponse getById(
            Long id,
            String ownerEmail
    ) {
        FeedingLog feedingLog = feedingLogRepository
                .findByIdAndPetOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feeding log not found"
                ));

        return feedingLogMapper.toResponse(feedingLog);
    }

    private FeedingLog createFromRequest(
            CreateFeedingLogRequest request,
            Pet pet,
            Food food
    ) {
        FeedingLog feedingLog = new FeedingLog();

        feedingLog.setPet(pet);
        feedingLog.setFood(food);
        feedingLog.setAmountGrams(request.amountGrams());
        feedingLog.setFedAt(request.fedAt());
        feedingLog.setNotes(request.notes());
        feedingLog.setCreatedAt(LocalDateTime.now());

        return feedingLog;
    }
}