package com.kaori.petnutritiontracker.weightrecord;

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
@Table(name = "weight_records")
public class WeightRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(nullable = false)
    private BigDecimal weightKg;

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}