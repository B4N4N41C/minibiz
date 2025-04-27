package ru.miniprog.minicrmapp.chat.internal.api.payload;

import java.util.Date;

public record MessageDto(
        Long id,
        String senderName,
        Long chatRoomId,
        String message,
        Date timestamp,
        String status
) {}
