package com.levelupfit.mainbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "http://localhost:4000")
@Controller
@RequiredArgsConstructor
@RequestMapping("routine")
public class RoutineController {

}
