package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.domain.routine.RoutineExercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineExercise.RoutineExerciseDTO;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseGetRequest;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExercisePatchRequest;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseRequest;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import com.levelupfit.mainbackend.repository.RoutineExerciseRepository;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {

    final RoutineExerciseRepository routineExerciseRepository;
    final RoutineRepository routineRepository;
    final ExerciseRepository exerciseRepository;

    //루틴 종복 생성
    public ApiResponse<List<RoutineExerciseDTO>> createRoutineExercise(List<RoutineExerciseRequest> routineExerciseDTOList) {
        try{
            int id = routineExerciseDTOList.get(0).getRoutineId();
            for(RoutineExerciseRequest routineExerciseDTO : routineExerciseDTOList) {
                if(routineExerciseDTO == null) {
                    return ApiResponse.fail("운동 정보가 없습니다.",400);
                } else if(routineExerciseDTO.getSets() != routineExerciseDTO.getReps().size()){
                    return ApiResponse.fail("sets 수보다 더 많은 reps를 지정할 수 없습니다.",400);
                }

                RoutineExercise routineExercise = RoutineExercise.builder()
                        .routine(routineRepository.findByRoutineId(routineExerciseDTO.getRoutineId()))
                        .exercise(exerciseRepository.findById(routineExerciseDTO.getExerciseId()))
                        .sets(routineExerciseDTO.getSets())
                        .reps(routineExerciseDTO.getReps().toArray(new Integer[0]))
                        .weight(routineExerciseDTO.getWeight().toArray(new Integer[0]))
                        .restTime(routineExerciseDTO.getRestTime())
                        .exerciseOrder(routineExerciseDTO.getRoutineId())
                        .build();

                routineExerciseRepository.save(routineExercise);
            }
            List<RoutineExerciseDTO> routineExerciseList = routineExerciseRepository.findByRoutineRoutineId(id)
                    .stream()
                    .map(RoutineExerciseDTO::fromRoutineExercise)
                    .toList();


            return ApiResponse.ok(routineExerciseList,200);
        } catch (Exception e){
            return ApiResponse.fail("루틴 운동 저장중 오류가 발생했습니다.",400);
        }
    }

    //루틴 종목 조회
    public ApiResponse<List<RoutineExerciseDTO>> getRoutineExercises(int routineId) {
        try {
            List<RoutineExerciseDTO> list = routineExerciseRepository.findByRoutineRoutineId(routineId)
                    .stream()
                    .map(RoutineExerciseDTO::fromRoutineExercise)
                    .toList();

            return ApiResponse.ok(list,200);
        } catch (Exception e){
            return ApiResponse.fail("루틴 종목 조회중 오류 발생", 500);
        }

    }

    //루틴 종목 수정
    @Transactional
    public ApiResponse<Void> patchRoutineExercise(RoutineExercisePatchRequest request) {
        try{
            List<RoutineExerciseDTO> existingList = request.getExistingRoutineExerciseList();
            List<RoutineExerciseRequest> newList = request.getNewRoutineExerciseList();

            if (existingList.isEmpty() && newList.isEmpty()) {
                return ApiResponse.fail("변경 요청이 비어 있음", 400);
            }

            int routineId = !existingList.isEmpty()
                    ? existingList.get(0).getRoutineId()
                    : newList.get(0).getRoutineId();

            List<RoutineExercise> dbList = routineExerciseRepository.findByRoutineRoutineId(routineId);
            Map<Integer, RoutineExercise> dbMap = dbList.stream()
                    .collect(Collectors.toMap(RoutineExercise::getId, r -> r));

            // ✅ 수정 및 유지 처리
            Set<Integer> incomingIds = new HashSet<>();
            for (RoutineExerciseDTO dto : existingList) {
                Integer id = dto.getId();
                incomingIds.add(id);

                RoutineExercise entity = dbMap.get(id);
                if (entity != null && isDifferent(entity, dto)) {
                    entity.updateFrom(dto, exerciseRepository.findById(dto.getExerciseId())); // 아래에 정의
                    routineExerciseRepository.save(entity);
                }
            }

            // ✅ 삭제 처리
            for (RoutineExercise dbItem : dbList) {
                if (!incomingIds.contains(dbItem.getId())) {
                    routineExerciseRepository.delete(dbItem);
                }
            }

            // ✅ 추가 처리
            Routine routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new EntityNotFoundException("루틴이 존재하지 않습니다."));

            for (RoutineExerciseRequest newReq : newList) {
                Exercise exercise = exerciseRepository.findById(newReq.getExerciseId());
                RoutineExercise newEntity = RoutineExercise.from(newReq, exercise);
                newEntity.setRoutine(routine);
                routineExerciseRepository.save(newEntity);
            }
            return ApiResponse.ok(null, 200);

        } catch (Exception e){
            return ApiResponse.fail(e.getMessage(), 500);
        }
    }

    //수정 감지
    private boolean isDifferent(RoutineExercise entity, RoutineExerciseDTO dto) {
        return !Objects.equals(entity.getExerciseOrder(), dto.getExerciseOrder()) ||
                !Objects.equals(entity.getSets(), dto.getSets()) ||
                !Objects.equals(convertArrayToList(entity.getReps()), dto.getReps()) ||
                !Objects.equals(convertArrayToList(entity.getWeight()), dto.getWeight()) ||
                !Objects.equals(entity.getExercise(), exerciseRepository.findById(dto.getExerciseId())) ||
                !Objects.equals(entity.getRestTime(), dto.getRestTime());
    }

    //Array -> List
    private List<Integer> convertArrayToList(Integer[] array) {
        return array == null ? new ArrayList<>() : Arrays.asList(array);
    }

    //List -> Array
    private Integer[] convertListToArray(List<Integer> list) {
        return list == null ? new Integer[]{} : list.toArray(new Integer[0]);
    }
}
