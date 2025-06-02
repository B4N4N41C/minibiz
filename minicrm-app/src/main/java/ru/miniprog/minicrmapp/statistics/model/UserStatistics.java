package ru.miniprog.minicrmapp.statistics.model;

public record UserStatistics(
        String username,
        long messages,
        long tasks,
        double profit
) {}
