package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineLogRepository extends JpaRepository<RoutineLogs, Integer> {
}
