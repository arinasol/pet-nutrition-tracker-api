package com.kaori.petnutritiontracker.weightrecord;

import com.kaori.petnutritiontracker.weightrecord.dto.CreateWeightRecordRequest;
import com.kaori.petnutritiontracker.weightrecord.dto.WeightRecordResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weight-records")
@RequiredArgsConstructor
public class WeightRecordController {

    private final WeightRecordService weightRecordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WeightRecordResponse createWeightRecord(
            @Valid @RequestBody CreateWeightRecordRequest request,
            Authentication authentication
    ) {
        return weightRecordService.createWeightRecord(
                request,
                authentication.getName()
        );
    }

    @GetMapping("/pet/{petId}")
    public List<WeightRecordResponse> getAllForPet(
            @PathVariable Long petId,
            Authentication authentication
    ) {
        return weightRecordService.getAllForPet(
                petId,
                authentication.getName()
        );
    }

    @GetMapping("/pet/{petId}/latest")
    public WeightRecordResponse getLatestForPet(
            @PathVariable Long petId,
            Authentication authentication
    ) {
        return weightRecordService.getLatestForPet(
                petId,
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWeightRecord(
            @PathVariable Long id,
            Authentication authentication
    ) {
        weightRecordService.deleteWeightRecord(
                id,
                authentication.getName()
        );
    }
}