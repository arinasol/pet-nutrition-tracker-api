package com.kaori.petnutritiontracker.feedinglog.mapper;

import com.kaori.petnutritiontracker.feedinglog.FeedingLog;
import com.kaori.petnutritiontracker.feedinglog.dto.FeedingLogResponse;
import org.springframework.stereotype.Component;

@Component
public class FeedingLogMapper {

    public FeedingLogResponse toResponse(FeedingLog feedingLog) {
        return new FeedingLogResponse(
                feedingLog.getId(),
                feedingLog.getPet().getId(),
                feedingLog.getPet().getName(),
                feedingLog.getFood().getId(),
                feedingLog.getFood().getName(),
                feedingLog.getAmountGrams(),
                feedingLog.getFedAt(),
                feedingLog.getNotes(),
                feedingLog.getCreatedAt()
        );
    }
}