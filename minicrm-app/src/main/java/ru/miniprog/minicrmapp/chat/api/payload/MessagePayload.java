package ru.miniprog.minicrmapp.chat.api.payload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для отправки сообщения в чат")
public record MessagePayload(
    @Schema(description = "Имя отправителя", example = "ivanov")
    String senderName,

    @Schema(description = "ID чат-комнаты", example = "1")
    Long chatRoom,

    @Schema(description = "Текст сообщения", example = "Привет!")
    String message,

    @Schema(description = "Статус сообщения", example = "MESSAGE")
    String status
) {}
