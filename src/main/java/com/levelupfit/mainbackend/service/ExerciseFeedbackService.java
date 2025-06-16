package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.feedback.ExerciseFeedbacksDTO;
import com.levelupfit.mainbackend.dto.feedback.request.ExerciseFeedbackRequest;
import com.levelupfit.mainbackend.repository.ExerciseFeedbackRepository;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExerciseFeedbackService {

    final ExerciseRepository exerciseRepository;
    final UserRepository userRepository;
    final ExerciseFeedbackRepository exerciseFeedbackRepository;

    public ApiResponse<ExerciseFeedbacksDTO> createFeedback(ExerciseFeedbackRequest request) {
        if(userRepository.existsByUserid(request.getUserId())) return ApiResponse.fail("유저 정보를 찾을 수 없습니다.",404);
        User user = userRepository.findByUserid(request.getUserId());
        try{
            if(exerciseRepository.existsById(request.getExerciseId())) {
                Exercise exercise = exerciseRepository.findById(request.getExerciseId());
                ExerciseFeedbacks exerciseFeedbacks = ExerciseFeedbacks
                        .builder()
                        .user(user)
                        .exercise(exercise)
                        .performedDate(request.getPerformedDate())
                        .build();
                exerciseFeedbackRepository.save(exerciseFeedbacks);

                ExerciseFeedbacksDTO dto = ExerciseFeedbacksDTO.fromEntity(exerciseFeedbacks);

                return ApiResponse.ok(dto,201);
            } else {
                return ApiResponse.fail("피드백을 지원하지 않는 운동입니다.",400);
            }
        } catch (Exception e){
            return ApiResponse.fail("피드백 영상 요청중 오류 발생", 500);
        }

    }

}
