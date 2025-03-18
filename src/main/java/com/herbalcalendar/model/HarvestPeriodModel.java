package com.herbalcalendar.model;

import com.herbalcalendar.enums.HarvestTime;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HarvestPeriodModel {

    @Enumerated(EnumType.STRING)
    private HarvestTime harvestTime;

    @Enumerated(EnumType.STRING)
    private Month harvestMonth;

    private String part;

    public LocalDate getHarvestStartDate() {
        return (harvestMonth != null) ? LocalDate.of(LocalDate.now().getYear(), harvestMonth, 1) : null;
    }
}
