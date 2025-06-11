package com.levelupfit.mainbackend.controller;


import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutineDeleteRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutinePatchRequest;
import com.levelupfit.mainbackend.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.jdbc.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("routine")
public class RoutineController {

    private final RoutineService routineService;

    //userId로 루틴 리스트 조회
    @GetMapping("/{userid}")
    public ResponseEntity<ApiResponse<List<RoutineDTO>>> getRoutine(@PathVariable Integer userid) {
        ApiResponse<List<RoutineDTO>> response = routineService.getRoutineByUserId(userid);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    //루틴 리스트 조회(기본제공)
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutineDTO>>> getRoutinesDefault() {
        ApiResponse<List<RoutineDTO>> response = routineService.getRoutineDefault();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
      
    //루틴 생성 메서드
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoutineDTO>> createRoutine(@RequestBody RoutineCreateRequest routineCreateRequest) {
        ApiResponse<RoutineDTO> response = routineService.createRoutine(routineCreateRequest);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    //루틴 삭제
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteRoutine(@RequestBody RoutineDeleteRequest routineDeleteRequest ) {
        ApiResponse<Void> response = routineService.deleteRoutine(routineDeleteRequest);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    //루틴 수정
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> patchRoutine(@RequestBody RoutinePatchRequest routinePatchRequest) {
        ApiResponse<Void> response = routineService.patchRoutine(routinePatchRequest);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
