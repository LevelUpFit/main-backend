package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Integer> {

}
