package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    @DisplayName("운동 생성 성공 시 생성된 운동 정보를 반환한다.")
    void createExercise_Success() {
        // Given
        ExerciseCreateRequest request = new ExerciseCreateRequest();
        request.setName("스쿼트");
        request.setDescription("하체 운동의 왕");
        request.setTargetMuscle("허벅지");

        Exercise exercise = Exercise.builder()
                .exerciseId(1)
                .name("스쿼트")
                .description("하체 운동의 왕")
                .targetMuscle("허벅지")
                .build();

        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        // When
        // 리팩토링 후의 메서드명인 createExercise를 호출하도록 테스트 작성
        ExerciseDTO result = exerciseService.createExercise(request);

        // Then
        assertNotNull(result);
        assertEquals("스쿼트", result.getName());
    }
}
