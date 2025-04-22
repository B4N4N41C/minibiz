package ru.miniprog.minicrm.api.payload;

public record UpdateTaskPayload(
        String title,
        String description
) {
}
