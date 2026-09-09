package com.kaori.petnutritiontracker.feedinglog.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FeedingLogResponse(
        Long id,
        Long petId,
        String petName,
        Long foodId,
        String foodName,
        BigDecimal amountGrams,
        LocalDateTime fedAt,
        String notes,
        LocalDateTime createdAt
) {
}