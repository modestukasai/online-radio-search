package com.modzo.ors.statistics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
class StatisticsController {

    private final StatisticsService statisticsService;

    StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    ResponseEntity<StatisticsResponse> createGenre() {
        Map<StatisticProvider.Type, Map<StatisticProvider.Subtype, String>> statistics = statisticsService.get();
        return ResponseEntity.ok(new StatisticsResponse(statistics));
    }
}
