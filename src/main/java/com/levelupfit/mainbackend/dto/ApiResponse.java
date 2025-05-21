package com.levelupfit.mainbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // 성공 응답용 팩토리 메서드
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "요청에 성공했습니다.", data);
    }

    // 실패 응답용 팩토리 메서드
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    //이걸로 반환하는거 먼저 해보고 고치기
}
