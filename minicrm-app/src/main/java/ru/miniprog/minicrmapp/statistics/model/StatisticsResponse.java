package ru.miniprog.minicrmapp.statistics.model;

import java.util.List;

public record StatisticsResponse(
        List<UserStatistics> userStatistics
) {}
