package com.herbalcalendar.model;

import com.herbalcalendar.enums.ActiveCompoundEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HERB")
public class HerbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HERB", columnDefinition = "TEXT", nullable = false)
    private String herb;

    @Column(name = "NAME", length = 128)
    private String latinName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVE_COMPOUNDS")
    private ActiveCompoundEnum activeCompoundEnum;

    @Embedded
    private HarvestPeriod harvestPeriod;

    @OneToMany(mappedBy = "herb", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHerbModel> userHerbs = new ArrayList<>();
}