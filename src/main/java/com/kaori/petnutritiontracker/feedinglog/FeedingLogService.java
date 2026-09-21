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
import com.kaori.petnutritiontracker.feedinglog.dto.UpdateFeedingLogRequest;
import com.kaori.petnutritiontracker.feedinglog.dto.DailySummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public FeedingLogResponse updateFeedingLog(
            Long id,
            UpdateFeedingLogRequest request,
            String ownerEmail
    ) {
        FeedingLog feedingLog = feedingLogRepository
                .findByIdAndPetOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feeding log not found"
                ));

        Food food = foodRepository
                .findByIdAndOwnerEmail(request.foodId(), ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Food not found"
                ));

        updateFromRequest(feedingLog, request, food);

        FeedingLog updatedLog = feedingLogRepository.save(feedingLog);

        return feedingLogMapper.toResponse(updatedLog);
    }

    public void deleteFeedingLog(
            Long id,
            String ownerEmail
    ) {
        FeedingLog feedingLog = feedingLogRepository
                .findByIdAndPetOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feeding log not found"
                ));

        feedingLogRepository.delete(feedingLog);
    }

    public DailySummaryResponse getDailySummary(
            Long petId,
            LocalDate date,
            String ownerEmail
    ) {
        Pet pet = petRepository.findByIdAndOwnerEmail(petId, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found"
                ));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<FeedingLog> logs = feedingLogRepository
                .findAllByPetIdAndPetOwnerEmailAndFedAtGreaterThanEqualAndFedAtLessThan(
                        petId,
                        ownerEmail,
                        start,
                        end
                );

        BigDecimal eatenGrams = logs.stream()
                .map(FeedingLog::getAmountGrams)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal targetGrams = pet.getDailyFoodTargetGrams();

        BigDecimal remainingGrams = targetGrams.subtract(eatenGrams);

        return new DailySummaryResponse(
                pet.getId(),
                pet.getName(),
                date,
                targetGrams,
                eatenGrams,
                remainingGrams
        );
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
    private void updateFromRequest(
            FeedingLog feedingLog,
            UpdateFeedingLogRequest request,
            Food food
    ) {
        feedingLog.setFood(food);
        feedingLog.setAmountGrams(request.amountGrams());
        feedingLog.setFedAt(request.fedAt());
        feedingLog.setNotes(request.notes());
    }
}