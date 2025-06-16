package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseFeedbackRepository extends JpaRepository<ExerciseFeedbacks,Integer> {
}
