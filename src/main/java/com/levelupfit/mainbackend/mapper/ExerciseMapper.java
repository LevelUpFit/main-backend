package com.levelupfit.mainbackend.mapper;

import com.levelupfit.mainbackend.dto.exercise.MybatisExercise;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExerciseMapper {
    List<MybatisExercise> findFeedbackExercises();
}
