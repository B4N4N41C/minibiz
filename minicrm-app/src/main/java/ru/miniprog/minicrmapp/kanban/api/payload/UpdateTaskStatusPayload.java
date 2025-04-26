package ru.miniprog.minicrmapp.kanban.api.payload;

public record UpdateTaskStatusPayload(
        Long task_id,
        Long status_id
) {
}
