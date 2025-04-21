package ru.miniprog.minicrm.api.payload;

public record MessagePayload(
        String senderName,
        Long chatRoom,
        String message,
        String status
) {
}
