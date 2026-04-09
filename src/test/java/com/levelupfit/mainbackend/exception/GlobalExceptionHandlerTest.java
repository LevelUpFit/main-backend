package com.levelupfit.mainbackend.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("BusinessException 발생 시 올바른 응답을 반환한다.")
    void handleBusinessExceptionTest() throws Exception {
        mockMvc.perform(post("/test/business-exception"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("Validation 실패 시 올바른 응답을 반환한다.")
    void handleValidationExceptionTest() throws Exception {
        String invalidJson = "{\"name\":\"\"}"; // NotBlank 위반

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("이름은 필수입니다."));
    }

    @RestController
    static class TestController {
        @PostMapping("/test/business-exception")
        public void throwBusinessException() {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        @PostMapping("/test/validation")
        public void throwValidationException(@Valid @RequestBody TestRequest request) {
        }
    }

    static class TestRequest {
        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        public TestRequest() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
