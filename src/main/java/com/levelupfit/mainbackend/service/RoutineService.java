package com.levelupfit.mainbackend.service;


import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutineDeleteRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutinePatchRequest;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    //루틴 생성
    public ApiResponse<RoutineDTO> createRoutine(RoutineCreateRequest routineCreateRequest) {
        try{
            String url = switch (routineCreateRequest.getTargetMuscle()) {
                case "하체" -> "levelupfit-profile/exercise/leg.png";
                case "가슴" -> "levelupfit-profile/exercise/chest.png";
                case "어깨" -> "levelupfit-profile/exercise/shoulder.png";
                case "팔" -> "levelupfit-profile/exercise/arm.png";
                case "등" -> "levelupfit-profile/exercise/back.png";
                default -> "levelupfit-profile/exercise/default.png";
            };

            Routine routine = Routine.builder()
                    .userId(routineCreateRequest.getUserId())
                    .name(routineCreateRequest.getName())
                    .targetMuscle(routineCreateRequest.getTargetMuscle())
                    .thumbnailUrl(url)
                    .description(routineCreateRequest.getDescription())
                    .difficulty(routineCreateRequest.getDifficulty())
                    .build();

            Routine createdRoutine = routineRepository.save(routine);

            RoutineDTO dto = RoutineDTO.fromRoutine(createdRoutine);

            return ApiResponse.ok(dto,201);

        } catch (Exception e){
            return ApiResponse.fail("루틴 생성중 오류 발생",500);
        }
    }
  
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

    //루틴 삭제
    public ApiResponse<Void> deleteRoutine(RoutineDeleteRequest routineDeleteRequest) {
        try{
            routineRepository.deleteById(routineDeleteRequest.getRoutineId());
            return ApiResponse.ok(null,201);
        } catch (Exception e){
            return ApiResponse.fail("삭제중 오류 발생", 500);
        }
    }

    //루틴 수정
    @Transactional
    public ApiResponse<Void> patchRoutine(RoutinePatchRequest routinePatchRequest) {
        try{
            Routine routine = routineRepository.findByRoutineId(routinePatchRequest.getRoutineId());
            routine.setName(routinePatchRequest.getName());
            routine.setDescription(routinePatchRequest.getDescription());
            routine.setDifficulty(routinePatchRequest.getDifficulty());



            return ApiResponse.ok(null,200);

        } catch (Exception e){
            return ApiResponse.fail("루틴 수정중 오류 발생", 500);
        }
    }
}
