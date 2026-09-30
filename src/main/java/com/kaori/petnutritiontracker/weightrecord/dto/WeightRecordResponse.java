package com.kaori.petnutritiontracker.weightrecord.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WeightRecordResponse(
        Long id,
        Long petId,
        String petName,
        BigDecimal weightKg,
        LocalDateTime measuredAt,
        String notes,
        LocalDateTime createdAt
) {
}