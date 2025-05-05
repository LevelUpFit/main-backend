package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "routine_item_sets")
public class RoutineItemSets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_set_id")
    private Long itemSetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_items_id", nullable = false)
    private RoutineItems routineItem;

    @Column(name = "set_num", nullable = false)
    private int setNum;

    @Column(name = "reps", nullable = false)
    private int reps;

    @Column(name = "weight")
    private float weight;

    @Column(name = "rest_time")
    private int restTime;
}
