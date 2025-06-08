package com.levelupfit.mainbackend.dto.UnifiedLog.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LogSearchRequest {
    private int userId;
    private LocalDate performedDate;
}
