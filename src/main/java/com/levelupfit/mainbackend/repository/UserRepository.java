package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //boolean existsByUsername(String username); // 중복 체크용
    //Optional<User> findByUsername(String username); // 로그인용

    boolean existsByUserid(int user_id);
    boolean existsByEmail(String email);
    User findByEmail(String email);


}
