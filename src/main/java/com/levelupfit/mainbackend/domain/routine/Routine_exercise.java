package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Routine_exercise {

    @Id
    @Column(name = "routine_exercises_id")
    private int routineExercisesId;

    @Column(name = "routine_id")
    private int routineId;

    @Column(name = "exercise_id")
    private int exerciseId;

    @Column(name = "sets")
    private int sets;

    @Column(name = "reps")
    private int reps;

    @Column(name = "rest_rime")
    private int restRime;

    @Column(name = "exercise_order")
    private int exerciseOrder;
}
