package ru.miniprog.minicrmapp.chat.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статусы сообщений")
public enum MessageStatus {
    @Schema(description = "Пользователь присоединился к чату")
    JOIN,
    @Schema(description = "Обычное сообщение")
    MESSAGE,
    @Schema(description = "Пользователь покинул чат")
    LEAVE
}