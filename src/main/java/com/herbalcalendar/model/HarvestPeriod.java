package com.herbalcalendar.model;

import com.herbalcalendar.enums.HarvestTime;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@Getter
@Embeddable
@NoArgsConstructor
@RequiredArgsConstructor
public class HarvestPeriod {
    @Enumerated(EnumType.STRING)
    private HarvestTime harvestTime;

    @Enumerated(EnumType.STRING)
    private Month harvestMonth;

    private String part;

    private LocalDate harvestStartDate;
}
