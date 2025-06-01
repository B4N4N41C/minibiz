package ru.miniprog.minicrmapp.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Типы чат-комнат")
public enum TypeChat {
    @Schema(description = "Чат между пользователями")
    USER_CHAT,
    @Schema(description = "Чат для обсуждения задачи")
    TASK_CHAT,
    @Schema(description = "Групповой чат")
    GROUP_CHAT
}
