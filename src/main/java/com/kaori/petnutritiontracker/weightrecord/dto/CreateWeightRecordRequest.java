package com.kaori.petnutritiontracker.weightrecord.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateWeightRecordRequest(

        @NotNull
        Long petId,

        @NotNull
        @Positive
        BigDecimal weightKg,

        @NotNull
        LocalDateTime measuredAt,

        String notes
) {
}