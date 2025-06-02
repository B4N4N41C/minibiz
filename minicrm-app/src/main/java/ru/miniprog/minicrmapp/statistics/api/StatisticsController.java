package ru.miniprog.minicrmapp.statistics.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.miniprog.minicrmapp.statistics.service.StatisticsService;
import ru.miniprog.minicrmapp.statistics.model.StatisticsResponse;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/dashboard")
    public StatisticsResponse getDashboardStatistics() {
        return statisticsService.getDashboardStatistics();
    }
}
