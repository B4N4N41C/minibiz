package ru.miniprog.minicrmapp.kanban.api.payload;

public record UpdateTaskPayload(
        String title,
        String description,
        Double profit
) {
}
