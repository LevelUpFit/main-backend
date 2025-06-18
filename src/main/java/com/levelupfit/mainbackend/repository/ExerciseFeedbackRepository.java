package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;
import com.levelupfit.mainbackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseFeedbackRepository extends JpaRepository<ExerciseFeedbacks,Integer> {
    ExerciseFeedbacks findByFeedbackId(int id);
    List<ExerciseFeedbacks> findAllByUser(User user);
}
