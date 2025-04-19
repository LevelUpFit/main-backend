package com.levelupfit.mainbackend.repository;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormUserRepository extends JpaRepository<FormUser, Integer> {

    //boolean existsByEmail(String email);
    FormUser findByUserId(int userId);
}
