package ru.miniprog.minicrmapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.miniprog.minicrmapp.statistics.api.StatisticsController;
import ru.miniprog.minicrmapp.statistics.model.StatisticsResponse;
import ru.miniprog.minicrmapp.statistics.model.UserStatistics;
import ru.miniprog.minicrmapp.statistics.service.StatisticsService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {
    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
    }

    @Test
    void getDashboardStatistics_ShouldReturnStatistics() throws Exception {
        // Подготовка тестовых данных
        List<UserStatistics> userStats = Arrays.asList(
                new UserStatistics("user1", 10, 5, 1000.0),
                new UserStatistics("user2", 15, 8, 2000.0)
        );

        StatisticsResponse expectedResponse = new StatisticsResponse(userStats);

        when(statisticsService.getDashboardStatistics()).thenReturn(expectedResponse);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/statistics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatistics", hasSize(2)))
                .andExpect(jsonPath("$.userStatistics[0].username", is("user1")))
                .andExpect(jsonPath("$.userStatistics[0].messages", is(10)))
                .andExpect(jsonPath("$.userStatistics[0].tasks", is(5)))
                .andExpect(jsonPath("$.userStatistics[0].profit", is(1000.0)))
                .andExpect(jsonPath("$.userStatistics[1].username", is("user2")))
                .andExpect(jsonPath("$.userStatistics[1].messages", is(15)))
                .andExpect(jsonPath("$.userStatistics[1].tasks", is(8)))
                .andExpect(jsonPath("$.userStatistics[1].profit", is(2000.0)));
    }

    @Test
    void getDashboardStatistics_WhenNoData_ShouldReturnEmptyList() throws Exception {
        // Подготовка тестовых данных
        StatisticsResponse expectedResponse = new StatisticsResponse(List.of());

        when(statisticsService.getDashboardStatistics()).thenReturn(expectedResponse);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/statistics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatistics", hasSize(0)));
    }
}
