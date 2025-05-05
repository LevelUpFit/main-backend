package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "routine_items")
@Getter
@Setter
public class RoutineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_items_id")
    private Long routineItemsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routines routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_exercise_id", nullable = false)
    private RoutineExercise routineExercise;

    @Column(name = "order_num", nullable = false)
    private int orderNum;
}
