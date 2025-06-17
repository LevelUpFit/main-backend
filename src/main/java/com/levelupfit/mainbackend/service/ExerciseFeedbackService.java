package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.feedback.ExerciseFeedbacksDTO;
import com.levelupfit.mainbackend.dto.feedback.request.ExerciseFeedbackRequest;
import com.levelupfit.mainbackend.dto.feedback.response.FeedbackresultDTO;
import com.levelupfit.mainbackend.repository.ExerciseFeedbackRepository;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ExerciseFeedbackService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ExerciseFeedbackRepository exerciseFeedbackRepository;
    private final FastApiWebClientService fastApiWebClientService;

    public ApiResponse<ExerciseFeedbacksDTO> createFeedback(ExerciseFeedbackRequest request) {
        if(!userRepository.existsByUserid(request.getUserId())) return ApiResponse.fail("유저 정보를 찾을 수 없습니다.",404);
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
                ExerciseFeedbacks saveDate = exerciseFeedbackRepository.save(exerciseFeedbacks);

                request.setFeedbackId(saveDate.getFeedbackId());
                sendVideo(request);

                ExerciseFeedbacksDTO dto = ExerciseFeedbacksDTO.fromEntity(exerciseFeedbacks);

                return ApiResponse.ok(dto,201);
            } else {
                return ApiResponse.fail("피드백을 지원하지 않는 운동입니다.",400);
            }
        } catch (Exception e){
            return ApiResponse.fail("피드백 영상 요청중 오류 발생", 500);
        }

    }

    public void sendVideo(ExerciseFeedbackRequest request) throws IOException {
        fastApiWebClientService.sendToFastApi(request)
                .subscribe(result -> {
                    // 성공 처리
                    System.out.println("결과: " + result);
                }, error -> {
                    // 에러 처리
                    System.out.println("asd");
                });
    }

    public void handleFeedbackResult(FeedbackresultDTO result){

    }

}
