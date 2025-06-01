package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineLogRepository extends JpaRepository<RoutineLogs, Integer> {
    List<RoutineLogs> findByUserId(int userId);
}
