package com.herbalcalendar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "user_herb")
public class UserHerbModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Zostawiamy id jako klucz główny

    @ManyToOne
    @JoinColumn(name = "herb_id", nullable = false)
    private HerbModel herb;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference // Zapobiega cyklicznym zależnościom
    private UserModel user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHerbModel that = (UserHerbModel) o;
        return id != null && id.equals(that.id);  // Porównujemy tylko ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

