package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;
import com.levelupfit.mainbackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Exercise findById(int id);
}
