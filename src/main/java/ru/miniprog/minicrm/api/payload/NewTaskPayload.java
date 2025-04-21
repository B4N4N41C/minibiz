package ru.miniprog.minicrm.api.payload;

public record NewTaskPayload(
    String title,
    String description,
    String time,
    Long status_id
) {}
