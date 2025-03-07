package com.herbalcalendar.model;

import com.herbalcalendar.enums.HarvestTime;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.Month;

@Getter
@Embeddable
@RequiredArgsConstructor
public class HarvestPeriod {
    @Enumerated(EnumType.STRING)
    private final HarvestTime harvestTime; // Pole finalne, wymaga wartości w konstruktorze

    @Enumerated(EnumType.STRING)
    private final Month harvestMonth; // Pole finalne, wymaga wartości w konstruktorze

    private final String part; // Można pozostać bez final (wtedy można ustawić setterem)

}
