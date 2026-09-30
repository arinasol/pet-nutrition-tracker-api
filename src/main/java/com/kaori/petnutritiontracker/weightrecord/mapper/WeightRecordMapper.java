package com.kaori.petnutritiontracker.weightrecord.mapper;

import com.kaori.petnutritiontracker.weightrecord.WeightRecord;
import com.kaori.petnutritiontracker.weightrecord.dto.WeightRecordResponse;
import org.springframework.stereotype.Component;

@Component
public class WeightRecordMapper {

    public WeightRecordResponse toResponse(WeightRecord weightRecord) {
        return new WeightRecordResponse(
                weightRecord.getId(),
                weightRecord.getPet().getId(),
                weightRecord.getPet().getName(),
                weightRecord.getWeightKg(),
                weightRecord.getMeasuredAt(),
                weightRecord.getNotes(),
                weightRecord.getCreatedAt()
        );
    }
}