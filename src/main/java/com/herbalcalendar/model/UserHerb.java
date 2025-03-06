package com.herbalcalendar.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_herb")
public class UserHerb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Zostawiamy id jako klucz główny

    @ManyToOne
    @JoinColumn(name = "herb_id", nullable = false)
    private HerbModel herb;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserModel user;
}

