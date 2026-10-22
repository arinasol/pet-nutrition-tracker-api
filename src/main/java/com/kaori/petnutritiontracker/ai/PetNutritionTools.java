package com.kaori.petnutritiontracker.ai;

import com.kaori.petnutritiontracker.feedinglog.FeedingLogService;
import com.kaori.petnutritiontracker.feedinglog.dto.DailySummaryResponse;
import com.kaori.petnutritiontracker.pet.PetService;
import com.kaori.petnutritiontracker.pet.dto.PetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PetNutritionTools {

    private final PetService petService;
    private final FeedingLogService feedingLogService;

    public PetResponse getPetDetails(
            Long petId,
            String ownerEmail
    ) {
        return petService.getPetById(petId, ownerEmail);
    }

    public DailySummaryResponse getDailyFeedingSummary(
            Long petId,
            LocalDate date,
            String ownerEmail
    ) {
        return feedingLogService.getDailySummary(
                petId,
                date,
                ownerEmail
        );
    }
    public WeeklyFeedingSummary getWeeklyFeedingSummary(
            Long petId,
            LocalDate endDate,
            String ownerEmail
    ) {
        LocalDate startDate = endDate.minusDays(6);

        var days = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> feedingLogService.getDailySummary(
                        petId, date, ownerEmail
                ))
                .toList();

        BigDecimal total = days.stream()
                .map(DailySummaryResponse::eatenGrams)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = total.divide(
                BigDecimal.valueOf(7),
                2,
                RoundingMode.HALF_UP
        );

        long daysBelowTarget = days.stream()
                .filter(day -> day.eatenGrams()
                        .compareTo(day.targetGrams()) < 0)
                .count();

        return new WeeklyFeedingSummary(
                petId,
                startDate,
                endDate,
                average,
                daysBelowTarget,
                days
        );
    }
}