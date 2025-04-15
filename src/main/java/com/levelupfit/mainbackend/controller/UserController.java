package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.LoginRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<String> signin(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok("success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto){
        if("test".equals(dto.getEmail()) && "test".equals(dto.getPassword())){
            return ResponseEntity.ok("Success");
        }
        else return ResponseEntity.ok("Fail");
    }
}
