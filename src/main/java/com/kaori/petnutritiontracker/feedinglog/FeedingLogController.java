package com.kaori.petnutritiontracker.feedinglog;

import com.kaori.petnutritiontracker.feedinglog.dto.CreateFeedingLogRequest;
import com.kaori.petnutritiontracker.feedinglog.dto.FeedingLogResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeding-logs")
@RequiredArgsConstructor
public class FeedingLogController {

    private final FeedingLogService feedingLogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedingLogResponse createFeedingLog(
            @Valid @RequestBody CreateFeedingLogRequest request,
            Authentication authentication
    ) {
        return feedingLogService.createFeedingLog(
                request,
                authentication.getName()
        );
    }

    @GetMapping("/pet/{petId}")
    public List<FeedingLogResponse> getAllForPet(
            @PathVariable Long petId,
            Authentication authentication
    ) {
        return feedingLogService.getAllForPet(
                petId,
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public FeedingLogResponse getById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return feedingLogService.getById(
                id,
                authentication.getName()
        );
    }
}