package ru.miniprog.minicrmapp.kanban.api.payload;

public record NewNote(
        String message,
        Long task_id
) {
}
