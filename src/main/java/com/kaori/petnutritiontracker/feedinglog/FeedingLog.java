package com.kaori.petnutritiontracker.feedinglog;

import com.kaori.petnutritiontracker.food.Food;
import com.kaori.petnutritiontracker.pet.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feeding_logs")
public class FeedingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private BigDecimal amountGrams;

    @Column(nullable = false)
    private LocalDateTime fedAt;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}