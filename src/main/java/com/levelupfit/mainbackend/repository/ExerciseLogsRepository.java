package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseLogsRepository extends JpaRepository<ExerciseLogs, Integer> {
}
