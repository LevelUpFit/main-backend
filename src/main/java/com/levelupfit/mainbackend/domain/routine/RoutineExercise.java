package com.levelupfit.mainbackend.domain.routine;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.routineExercise.RoutineExerciseDTO;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseRequest;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "routine_exercises")
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routines_exercises_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private int sets;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "integer[]", nullable = false)
    private Integer[] reps;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "integer[]", nullable = false)
    private Integer[] weight;

    @Column(name = "rest_time")
    private Integer restTime; //null 허용을 위해 Integer 타입 선택

    @Column(name = "exercise_order", nullable = false)
    private int exerciseOrder;

    public void updateFrom(RoutineExerciseDTO dto, Exercise exercise) {
        this.exerciseOrder = dto.getExerciseOrder();
        this.setExercise(exercise);
        this.sets = dto.getSets();
        this.reps = dto.getReps().toArray(new Integer[0]);
        this.reps = dto.getWeight().toArray(new Integer[0]);
        this.restTime = dto.getRestTime();
    }


    public static RoutineExercise from(RoutineExerciseRequest req, Exercise exercise) {
        RoutineExercise re = new RoutineExercise();
        re.setExercise(exercise);
        re.setSets(req.getSets());
        re.setReps(req.getReps().toArray(new Integer[0]));
        re.setWeight(req.getReps().toArray(new Integer[0]));
        re.setRestTime(req.getRestTime());
        re.setExerciseOrder(req.getExerciseOrder());
        // Routine은 service에서 set 해줌
        return re;
    }
}
