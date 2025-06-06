package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.user.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepository extends JpaRepository<SocialUser, Integer> {
    boolean existsByEmail(String email);


}
