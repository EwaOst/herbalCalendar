package com.herbalcalendar.model;

import jakarta.persistence.*;

@Entity
public class LocationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    // getters and setters
}