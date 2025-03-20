package ru.miniprog.minicrm.api.payload;

import ru.miniprog.minicrm.model.Status;

import java.util.Date;

public record NewTaskPayload(
        String title,
        String description,
        String time,
        Long status_id
) {
}
