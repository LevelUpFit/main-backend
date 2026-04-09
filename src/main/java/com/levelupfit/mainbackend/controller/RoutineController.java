package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutineDeleteRequest;
import com.levelupfit.mainbackend.dto.routine.request.RoutinePatchRequest;
import com.levelupfit.mainbackend.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routine")
public class RoutineController {

    private final RoutineService routineService;

    /**
     * 유저 ID로 루틴 리스트 조회
     */
    @GetMapping("/{userid}")
    public ResponseEntity<ApiResponse<List<RoutineDTO>>> getRoutine(@PathVariable Integer userid) {
        List<RoutineDTO> result = routineService.getRoutineByUserId(userid);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 기본 루틴 리스트 조회 (관리자 제공)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutineDTO>>> getRoutinesDefault() {
        List<RoutineDTO> result = routineService.getRoutineDefault();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
      
    /**
     * 루틴 생성
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoutineDTO>> createRoutine(@RequestBody RoutineCreateRequest request) {
        RoutineDTO result = routineService.createRoutine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201, result));
    }

    /**
     * 루틴 삭제
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteRoutine(@RequestBody RoutineDeleteRequest request) {
        routineService.deleteRoutine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201));
    }

    /**
     * 루틴 수정
     */
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> patchRoutine(@RequestBody RoutinePatchRequest request) {
        routineService.patchRoutine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201));
    }
}
