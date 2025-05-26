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
import java.util.List;


@CrossOrigin(origins = "http://localhost:4000")
@Controller
@RequiredArgsConstructor
@RequestMapping("routine")  
public class RoutineController {

    private final RoutineService routineService;

    //userId로 조회
    @GetMapping("/{userid}")
    public ResponseEntity<ApiResponse<List<RoutineDTO>>> getRoutine(@PathVariable Integer userid) {
        ApiResponse<List<RoutineDTO>> response = routineService.getRoutineByUserId(userid);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.badRequest().body(response);
        }
    }

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
}
