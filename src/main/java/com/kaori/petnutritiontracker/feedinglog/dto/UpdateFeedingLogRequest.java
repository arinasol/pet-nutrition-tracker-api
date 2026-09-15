package com.kaori.petnutritiontracker.feedinglog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateFeedingLogRequest(

        @NotNull
        Long foodId,

        @NotNull
        @Positive
        BigDecimal amountGrams,

        @NotNull
        LocalDateTime fedAt,

        String notes
) {
}