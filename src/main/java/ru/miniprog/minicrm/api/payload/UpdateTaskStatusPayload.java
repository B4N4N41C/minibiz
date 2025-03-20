package ru.miniprog.minicrm.api.payload;

public record UpdateTaskStatusPayload(
        Long task_id,
        Long status_id
) {
}
