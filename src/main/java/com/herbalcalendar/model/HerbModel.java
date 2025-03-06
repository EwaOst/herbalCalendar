package com.herbalcalendar.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
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
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACTIVE_COMPOUNDS")
    private String activeCompounds;

    @Column(name = "HARVEST_TIME")
    private LocalDate harvestTime;

    @OneToMany(mappedBy = "herb", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHerb> userHerbs = new ArrayList<>();
}