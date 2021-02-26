package com.cinema.service.mapper.impl;

import com.cinema.model.Performance;
import com.cinema.model.PerformanceSession;
import com.cinema.model.Stage;
import com.cinema.model.dto.PerformanceSessionRequestDto;
import com.cinema.model.dto.PerformanceSessionResponseDto;
import com.cinema.service.PerformanceService;
import com.cinema.service.StageService;
import com.cinema.service.mapper.PerformanceSessionMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerformanceSessionMapperImpl implements PerformanceSessionMapper {
    private final PerformanceService performanceService;
    private final StageService stageService;

    @Autowired
    public PerformanceSessionMapperImpl(PerformanceService performanceService,
                                        StageService stageService) {
        this.performanceService = performanceService;
        this.stageService = stageService;
    }

    @Override
    public PerformanceSessionResponseDto toPerformanceSessionDto(PerformanceSession
                                                                         performanceSession) {
        PerformanceSessionResponseDto responseDto = new PerformanceSessionResponseDto();
        responseDto.setId(performanceSession.getId());
        responseDto.setPerformanceTitle(performanceSession.getPerformance().getTitle());
        responseDto.setStageId(performanceSession.getStage().getId());
        responseDto.setStageCapacity(performanceSession.getStage().getCapacity());
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        String formattedDateTime = performanceSession.getShowTime().format(formatter);
        responseDto.setShowTime(formattedDateTime);
        return responseDto;
    }

    @Override
    public PerformanceSession toPerformanceSessionEntity(PerformanceSessionRequestDto
                                                                 performanceSessionRequestDto) {
        PerformanceSession performanceSession = new PerformanceSession();
        Performance performance = performanceService
                .get(performanceSessionRequestDto.getPerformanceId());
        performanceSession.setPerformance(performance);
        Stage stage = stageService.get(performanceSessionRequestDto.getStageId());
        performanceSession.setStage(stage);
        String dateString = performanceSessionRequestDto.getShowTime();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime showTime = LocalDateTime.parse(dateString, formatter);
        performanceSession.setShowTime(showTime);
        return performanceSession;
    }
}
