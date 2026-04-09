package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.repository.RoutineRepository;
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
class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @InjectMocks
    private RoutineService routineService;

    @Test
    @DisplayName("루틴 생성 성공 시 생성된 루틴 정보를 반환한다.")
    void createRoutine_Success() {
        // Given
        RoutineCreateRequest request = new RoutineCreateRequest();
        request.setUserId(1);
        request.setName("가슴 운동 루틴");
        request.setTargetMuscle("가슴");
        request.setDescription("기본 가슴 루틴");
        request.setDifficulty(1);

        Routine routine = Routine.builder()
                .routineId(1)
                .userId(1)
                .name("가슴 운동 루틴")
                .targetMuscle("가슴")
                .thumbnailUrl("levelupfit-profile/exercise/chest.png")
                .description("기본 가슴 루틴")
                .difficulty(1)
                .build();

        when(routineRepository.save(any(Routine.class))).thenReturn(routine);

        // When
        RoutineDTO result = routineService.createRoutine(request);

        // Then
        assertNotNull(result);
        assertEquals("가슴 운동 루틴", result.getName());
        assertEquals("levelupfit-profile/exercise/chest.png", result.getThumbnailUrl());
    }
}
