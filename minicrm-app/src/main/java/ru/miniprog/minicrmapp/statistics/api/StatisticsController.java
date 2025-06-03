package ru.miniprog.minicrmapp.statistics.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.miniprog.minicrmapp.kanban.api.KanbanBoardController;
import ru.miniprog.minicrmapp.statistics.service.StatisticsService;
import ru.miniprog.minicrmapp.statistics.model.StatisticsResponse;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    Logger log = LoggerFactory.getLogger(StatisticsController.class);

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/dashboard")
    public StatisticsResponse getDashboardStatistics() {
        log.info("Запрос на получение статистики дашборда");
        StatisticsResponse statistics = statisticsService.getDashboardStatistics();
        log.info("Статистика успешно получена");
        return statistics;
    }
}
