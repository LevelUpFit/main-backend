package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4000")
@Controller
@RequiredArgsConstructor
@RequestMapping("routine")
public class RoutineController {

    private final RoutineService routineService;

    //루틴 생성 메서드
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RoutineDTO>> createRoutine(@RequestBody RoutineCreateRequest routineCreateRequest) {
        ApiResponse<RoutineDTO> response = routineService.createRoutine(routineCreateRequest);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.badRequest().body(response);
        }
    }
}
