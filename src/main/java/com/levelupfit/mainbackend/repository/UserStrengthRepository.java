package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.domain.user.UserStrength;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStrengthRepository extends JpaRepository<UserStrength, Integer> {
    boolean existsByUserId(int userId);
    UserStrength findByUserId(int userId);
}