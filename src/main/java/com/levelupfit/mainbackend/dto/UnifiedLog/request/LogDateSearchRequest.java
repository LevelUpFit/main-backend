package com.levelupfit.mainbackend.dto.UnifiedLog.request;

import lombok.Data;

@Data
public class LogDateSearchRequest {
    private int userId;
    private int year;
    private int month;
}
