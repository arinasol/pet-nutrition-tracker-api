package com.kaori.petnutritiontracker.ai;

import com.kaori.petnutritiontracker.pet.dto.PetResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.kaori.petnutritiontracker.feedinglog.dto.DailySummaryResponse;
import java.time.LocalDate;


import java.util.function.Function;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final ChatClient chatClient;
    private final PetNutritionTools petNutritionTools;
    private final AiErrorHandler aiErrorHandler;

    public AiController(
            ChatClient.Builder builder,
            PetNutritionTools petNutritionTools,
            AiErrorHandler aiErrorHandler
    ) {
        this.chatClient = builder.build();
        this.petNutritionTools = petNutritionTools;
        this.aiErrorHandler = aiErrorHandler;
    }

    public record GetPetRequest(Long petId) {}
    public record GetDailySummaryRequest(
            Long petId,
            LocalDate date
    ) {}
    public record GetWeeklySummaryRequest(
            Long petId,
            LocalDate endDate
    ) {}

    @PostMapping("/chat")
    public String chat(
            @RequestBody String message,
            Authentication authentication
    ) {
        String ownerEmail = authentication.getName();

        Function<GetPetRequest, PetResponse> getPet =
                request -> petNutritionTools.getPetDetails(
                        request.petId(),
                        ownerEmail
                );

        var getPetTool = FunctionToolCallback
                .builder("getPetDetails", getPet)
                .description("Get pet details by pet ID. Use this tool when the user asks about a specific pet.")
                .inputType(GetPetRequest.class)
                .build();
        Function<GetDailySummaryRequest, DailySummaryResponse> getDailySummary =
                request -> petNutritionTools.getDailyFeedingSummary(
                        request.petId(),
                        request.date(),
                        ownerEmail
                );

        var getDailySummaryTool = FunctionToolCallback
                .builder("getDailyFeedingSummary", getDailySummary)
                .description("""
                Get a pet's feeding summary for a specific date.
                Returns the daily food target, eaten grams and remaining grams.
                Use this tool when the user asks about food consumption.
                """)
                .inputType(GetDailySummaryRequest.class)
                .build();

        Function<GetWeeklySummaryRequest, WeeklyFeedingSummary> getWeeklySummary =
                request -> petNutritionTools.getWeeklyFeedingSummary(
                        request.petId(),
                        request.endDate(),
                        ownerEmail
                );

        var getWeeklySummaryTool = FunctionToolCallback
                .builder("getWeeklyFeedingSummary", getWeeklySummary)
                .description("""
                Analyze recorded pet feeding over seven days,
                ending on the specified date.
                Returns daily summaries, average recorded
                food consumption and days below target.
                """)
                .inputType(GetWeeklySummaryRequest.class)
                .build();
        try {
        return chatClient.prompt()
                .system("""
                    You are a pet nutrition assistant.
            
                    Use the available tools to retrieve real pet information
                    and feeding summaries.
            
                    Never invent pet data or feeding amounts.
                    If no feeding was recorded, explain that no intake
                    was recorded rather than assuming the pet did not eat.
            
                    Do not provide veterinary diagnoses.
                    Respond in the same language as the user.
                    
                    When analyzing weekly feeding, distinguish recorded
                    food consumption from actual food consumption.
                    Missing records do not prove that the pet was not fed.
                    """)
                .user(message)
                .toolCallbacks(
                        getPetTool,
                        getDailySummaryTool,
                        getWeeklySummaryTool
                )
                .call()
                .content();
            } catch (RuntimeException exception) {
            throw aiErrorHandler.translate(exception);
            }
    }
}