package com.kaori.petnutritiontracker.ai;

import com.kaori.petnutritiontracker.feedinglog.dto.DailySummaryResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record WeeklyFeedingSummary(
        Long petId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal averageEatenGrams,
        long daysBelowTarget,
        List<DailySummaryResponse> days
) {}