package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsGetRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.repository.ExerciseLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseLogsService {

    private final ExerciseLogsRepository exerciseLogsRepository;

    /**
     * 운동 기록 저장
     */
    @Transactional
    public void saveExerciseLog(ExerciseLogsRequest request) {
        String[] names = request.getName().split(",");

        for (String name : names) {
            ExerciseLogs log = ExerciseLogs.of(
                    request.getUserId(),
                    name.trim(),
                    request.getTargetMuscle(),
                    request.getFeedback(),
                    request.getPerformedDate()
            );
            exerciseLogsRepository.save(log);
        }
    }
    
    /**
     * 운동 기록 조회
     */
    public List<ExerciseLogsDTO> getExerciseLogs(ExerciseLogsGetRequest request) {
        return exerciseLogsRepository.findAllByUserId(request.getUserId())
                .stream()
                .map(ExerciseLogsDTO::fromExerciseLogs)
                .toList();
    }
    
    /**
     * 운동 기록 삭제
     */
    @Transactional
    public void deleteExerciseLog(ExerciseLogsDeleteRequest request) {
        exerciseLogsRepository.deleteById(request.getExerciseLogId());
    }
}
