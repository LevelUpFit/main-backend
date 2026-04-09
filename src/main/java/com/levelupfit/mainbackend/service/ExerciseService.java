package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.MybatisExercise;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.mapper.ExerciseMapper;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    /**
     * 운동 생성
     */
    @Transactional
    public ExerciseDTO createExercise(ExerciseCreateRequest request) {
        Exercise exercise = Exercise.of(request);
        Exercise savedExercise = exerciseRepository.save(exercise);
        return ExerciseDTO.fromExercise(savedExercise);
    }
    
    /**
     * 모든 운동 조회
     */
    public List<ExerciseDTO> findAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(ExerciseDTO::fromExercise)
                .toList();
    }

    /**
     * 운동 단일 조회
     */
    public ExerciseDTO findById(int id) {
        Exercise exercise = exerciseRepository.findById(id);
        return ExerciseDTO.fromExercise(exercise);
    }

    /**
     * 피드백 가능 운동 조회 (MyBatis)
     */
    public List<MybatisExercise> findFeedbackExercises() {
        return exerciseMapper.findFeedbackExercises();
    }
}
