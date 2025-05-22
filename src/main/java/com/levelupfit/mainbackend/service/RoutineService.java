package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    //유저 ID로 루틴 조회
    public ApiResponse<List<RoutineDTO>> getRoutineByUserId(Integer userid) {
        try{
            List<RoutineDTO> list = routineRepository.findByUserId(userid)
                    .stream()
                    .map(RoutineDTO::fromRoutine)
                    .toList();

            return ApiResponse.ok(list,200);
        } catch (Exception e){
            return ApiResponse.fail("루틴 조회중 오류가 발생하였습니다",500);
        }
    }

    //기본 루틴 조회
    public ApiResponse<List<RoutineDTO>> getRoutineDefault() {
        try{
            List<RoutineDTO> list = routineRepository.findByUserIdIsNull()
                    .stream()
                    .map(RoutineDTO::fromRoutine)
                    .toList();
            return ApiResponse.ok(list,200);
        } catch (Exception e){
            return ApiResponse.fail("루틴 조회중 오류 발생",500);
        }
    }
}
