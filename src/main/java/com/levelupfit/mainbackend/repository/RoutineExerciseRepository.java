package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.routine.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Integer> {
    List<RoutineExercise> findByRoutineRoutineId(int routineId);
}
