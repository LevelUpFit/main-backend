package com.levelupfit.mainbackend.service.feedback;

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
import com.levelupfit.mainbackend.service.FastApiWebClientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseFeedbackService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ExerciseFeedbackRepository exerciseFeedbackRepository;
    private final FastApiWebClientService fastApiWebClientService;
    private final FeedbacksUpdateService feedbacksUpdateService;

    // 피드백 초안 저장
    public ApiResponse<ExerciseFeedbacksDTO> createFeedback(ExerciseFeedbackRequest request) {
        if(!userRepository.existsByUserid(request.getUserId())) return ApiResponse.fail(404, "유저 정보를 찾을 수 없습니다.");
        User user = userRepository.findByUserid(request.getUserId());
        try{
            if(exerciseRepository.existsById(request.getExerciseId())) {
                Exercise exercise = exerciseRepository.findById(request.getExerciseId());
                ExerciseFeedbacks exerciseFeedbacks = ExerciseFeedbacks
                        .builder()
                        .user(user)
                        .exercise(exercise)
                        .level(request.getLevel())
                        .performedDate(request.getPerformedDate())
                        .build();
                ExerciseFeedbacks saveDate = exerciseFeedbackRepository.save(exerciseFeedbacks);

                request.setFeedbackId(saveDate.getFeedbackId());
                sendVideo(request);

                ExerciseFeedbacksDTO dto = ExerciseFeedbacksDTO.fromEntity(exerciseFeedbacks);

                return ApiResponse.ok(201, dto);
            } else {
                return ApiResponse.fail(400, "피드백을 지원하지 않는 운동입니다.");
            }
        } catch (Exception e){
            return ApiResponse.fail(500, "피드백 영상 요청 중 오류 발생");
        }

    }

    // FastApi로 동영상 전송
    public void sendVideo(ExerciseFeedbackRequest request) throws IOException {
        fastApiWebClientService.sendToFastApi(request)
                .subscribe(result -> {
                    feedbacksUpdateService.updateFeedback(result);
                    System.out.println("결과: " + result);
                }, error -> {
                    System.out.println("error: " + error.getMessage());
                });
    }

    // 피드백 기록 조회 (회원별)
    public ApiResponse<List<ExerciseFeedbacksDTO>> getFeedbackByUserId(int userId) {
        try{
            if(userRepository.existsByUserid(userId)) {
                User user = userRepository.findByUserid(userId);
                List<ExerciseFeedbacksDTO> dto = exerciseFeedbackRepository.findAllByUser(user)
                        .stream()
                        .map(ExerciseFeedbacksDTO::fromEntity)
                        .toList();

                return ApiResponse.ok(dto);
            } else {
                return ApiResponse.fail(404, "유저 정보를 조회할 수 없습니다.");
            }
        } catch (Exception e) {
            return ApiResponse.fail(500, "피드백 조회 중 오류 발생");
        }
    }

    // 피드백 단일 조회
    public ApiResponse<ExerciseFeedbacksDTO> getFeedbackById(int feedbackId) {
        try{
            ExerciseFeedbacksDTO result = ExerciseFeedbacksDTO.fromEntity(exerciseFeedbackRepository.findByFeedbackId(feedbackId));
            return ApiResponse.ok(result);
        } catch (Exception e) {
            return ApiResponse.fail(500, "피드백 조회 실패");
        }
    }

}
