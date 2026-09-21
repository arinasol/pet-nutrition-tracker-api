package com.kaori.petnutritiontracker.feedinglog.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySummaryResponse(
        Long petId,
        String petName,
        LocalDate date,
        BigDecimal targetGrams,
        BigDecimal eatenGrams,
        BigDecimal remainingGrams
) {
}