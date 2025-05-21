package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

}
