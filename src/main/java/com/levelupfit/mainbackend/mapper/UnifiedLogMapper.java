package com.levelupfit.mainbackend.mapper;

import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDate;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UnifiedLogMapper {
    List<UnifiedLogDto> findAllLogsByUserId(@Param("userId") int userId);
    List<UnifiedLogDto> findAllLogsByUserIdAndDate(@Param("userId") int userId , @Param("performedDate") LocalDate performedDate);
    List<LocalDate> findLogDatesByUserAndMonth(@Param("userId") int userId, @Param("year") int year, @Param("month") int month);
}
