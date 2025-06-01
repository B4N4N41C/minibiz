package ru.miniprog.minicrmapp.chat.api.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "DTO для передачи данных о сообщении")
public record MessageDto(
    @Schema(description = "Уникальный идентификатор сообщения", example = "1")
    Long id,

    @Schema(description = "Имя отправителя", example = "ivanov")
    String senderName,

    @Schema(description = "ID чат-комнаты", example = "1")
    Long chatRoom,

    @Schema(description = "Текст сообщения", example = "Привет!")
    String message,

    @Schema(description = "Дата и время отправки сообщения")
    Date date,

    @Schema(description = "Статус сообщения", example = "MESSAGE")
    String status
) {}
