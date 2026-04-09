package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.repository.ExerciseLogsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExerciseLogsServiceTest {

    @Mock
    private ExerciseLogsRepository exerciseLogsRepository;

    @InjectMocks
    private ExerciseLogsService exerciseLogsService;

    @Test
    @DisplayName("운동 기록 저장 시 콤마로 구분된 이름만큼 저장 로직이 호출된다.")
    void saveExerciseLog_MultipleNames() {
        // Given
        ExerciseLogsRequest request = new ExerciseLogsRequest();
        request.setUserId(1);
        request.setName("스쿼트, 데드리프트, 벤치프레스");
        request.setTargetMuscle("전신");
        request.setPerformedDate(LocalDate.now());

        // When
        exerciseLogsService.saveExerciseLog(request);

        // Then
        verify(exerciseLogsRepository, times(3)).save(any(ExerciseLogs.class));
    }
}
