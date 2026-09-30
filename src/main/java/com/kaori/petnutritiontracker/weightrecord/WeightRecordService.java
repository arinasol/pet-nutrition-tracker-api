package com.kaori.petnutritiontracker.weightrecord;

import com.kaori.petnutritiontracker.pet.Pet;
import com.kaori.petnutritiontracker.pet.PetRepository;
import com.kaori.petnutritiontracker.weightrecord.dto.CreateWeightRecordRequest;
import com.kaori.petnutritiontracker.weightrecord.dto.WeightRecordResponse;
import com.kaori.petnutritiontracker.weightrecord.mapper.WeightRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;
    private final PetRepository petRepository;
    private final WeightRecordMapper weightRecordMapper;

    public WeightRecordResponse createWeightRecord(
            CreateWeightRecordRequest request,
            String ownerEmail
    ) {
        Pet pet = petRepository
                .findByIdAndOwnerEmail(request.petId(), ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found"
                ));

        WeightRecord weightRecord = createFromRequest(request, pet);

        WeightRecord savedWeightRecord =
                weightRecordRepository.save(weightRecord);

        return weightRecordMapper.toResponse(savedWeightRecord);
    }

    public List<WeightRecordResponse> getAllForPet(
            Long petId,
            String ownerEmail
    ) {
        return weightRecordRepository
                .findAllByPetIdAndPetOwnerEmailOrderByMeasuredAtDesc(
                        petId,
                        ownerEmail
                )
                .stream()
                .map(weightRecordMapper::toResponse)
                .toList();
    }

    public WeightRecordResponse getLatestForPet(
            Long petId,
            String ownerEmail
    ) {
        WeightRecord weightRecord = weightRecordRepository
                .findFirstByPetIdAndPetOwnerEmailOrderByMeasuredAtDesc(
                        petId,
                        ownerEmail
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Weight record not found"
                ));

        return weightRecordMapper.toResponse(weightRecord);
    }

    public void deleteWeightRecord(
            Long id,
            String ownerEmail
    ) {
        WeightRecord weightRecord = weightRecordRepository
                .findByIdAndPetOwnerEmail(id, ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Weight record not found"
                ));

        weightRecordRepository.delete(weightRecord);
    }

    private WeightRecord createFromRequest(
            CreateWeightRecordRequest request,
            Pet pet
    ) {
        WeightRecord weightRecord = new WeightRecord();

        weightRecord.setPet(pet);
        weightRecord.setWeightKg(request.weightKg());
        weightRecord.setMeasuredAt(request.measuredAt());
        weightRecord.setNotes(request.notes());
        weightRecord.setCreatedAt(LocalDateTime.now());

        return weightRecord;
    }
}