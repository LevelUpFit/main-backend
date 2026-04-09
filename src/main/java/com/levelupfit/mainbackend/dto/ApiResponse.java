package com.levelupfit.mainbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> ok(T data) {
        return ok(200, data);
    }

    // 성공 응답 (데이터 및 코드 포함)
    public static <T> ApiResponse<T> ok(int code, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message("성공")
                .data(data)
                .build();
    }

    // 성공 응답 (데이터 미포함)
    public static ApiResponse<Void> ok() {
        return ok(200);
    }

    // 성공 응답 (코드 포함, 데이터 미포함)
    public static ApiResponse<Void> ok(int code) {
        return ApiResponse.<Void>builder()
                .success(true)
                .code(code)
                .message("성공")
                .data(null)
                .build();
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(int code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
