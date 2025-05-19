package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Routine {

    @Id
    @Column(name = "routines_id")
    private int routinesId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "difficulty")
    private int difficulty;

    @Column(name = "created_at")
    private String createdAt;
}
