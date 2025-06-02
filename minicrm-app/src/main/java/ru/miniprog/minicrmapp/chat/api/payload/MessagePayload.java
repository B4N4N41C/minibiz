package ru.miniprog.minicrmapp.chat.api.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO для отправки сообщения в чат")
public record MessagePayload(
    @Schema(description = "Имя отправителя", example = "ivanov")
    @NotBlank(message = "Имя отправителя не может быть пустым")
    @Size(min = 3, max = 50, message = "Имя отправителя должно быть от 3 до 50 символов")
    String senderName,

    @Schema(description = "ID чат-комнаты", example = "1")
    @NotNull(message = "ID чат-комнаты не может быть пустым")
    Long chatRoom,

    @Schema(description = "Текст сообщения", example = "Привет!")
    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(max = 1000, message = "Сообщение не может быть длиннее 1000 символов")
    String message,

    @Schema(description = "Статус сообщения", example = "MESSAGE")
    @NotBlank(message = "Статус не может быть пустым")
    String status
) {}
