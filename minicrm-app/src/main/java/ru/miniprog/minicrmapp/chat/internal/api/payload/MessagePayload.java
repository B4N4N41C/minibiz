package ru.miniprog.minicrmapp.chat.internal.api.payload;

public record MessagePayload(
        String senderName,
        Long chatRoom,
        String message,
        String status
) {
}
