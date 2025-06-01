package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseLogsRepository extends JpaRepository<ExerciseLogs, Integer> {

    List<ExerciseLogs> findAllByUserId(int userId);
}
