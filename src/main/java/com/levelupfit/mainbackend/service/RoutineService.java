package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutineDeleteRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutinePatchRequest;
import com.levelupfit.mainbackend.exception.BusinessException;
import com.levelupfit.mainbackend.exception.ErrorCode;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {

    private final RoutineRepository routineRepository;

    /**
     * 루틴 생성
     */
    @Transactional
    public RoutineDTO createRoutine(RoutineCreateRequest request) {
        Routine routine = Routine.of(request);
        Routine savedRoutine = routineRepository.save(routine);
        return RoutineDTO.fromRoutine(savedRoutine);
    }
  
    /**
     * 유저 ID로 루틴 조회
     */
    public List<RoutineDTO> getRoutineByUserId(Integer userId) {
        return routineRepository.findByUserId(userId)
                .stream()
                .map(RoutineDTO::fromRoutine)
                .toList();
    }

    /**
     * 기본 루틴 조회 (userId가 null인 항목)
     */
    public List<RoutineDTO> getRoutineDefault() {
        return routineRepository.findByUserIdIsNull()
                .stream()
                .map(RoutineDTO::fromRoutine)
                .toList();
    }

    /**
     * 루틴 삭제
     */
    @Transactional
    public void deleteRoutine(RoutineDeleteRequest request) {
        routineRepository.deleteById(request.getRoutineId());
    }

    /**
     * 루틴 수정
     */
    @Transactional
    public void patchRoutine(RoutinePatchRequest request) {
        Routine routine = routineRepository.findByRoutineId(request.getRoutineId());
        if (routine == null) {
            // TODO: 필요한 경우 ROUTINE_NOT_FOUND 에러 코드 추가
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "해당 루틴을 찾을 수 없습니다.");
        }
        routine.setName(request.getName());
        routine.setDescription(request.getDescription());
        routine.setDifficulty(request.getDifficulty());
    }
}
