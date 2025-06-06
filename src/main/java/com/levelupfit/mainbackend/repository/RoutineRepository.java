package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Integer> {
    Routine findByRoutineId (int id);
    List<Routine> findByUserId(Integer userId);
    List<Routine> findByUserIdIsNull();

}
