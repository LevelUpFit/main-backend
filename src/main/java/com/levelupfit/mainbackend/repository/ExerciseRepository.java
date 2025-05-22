package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Exercise findById(int id);
}
