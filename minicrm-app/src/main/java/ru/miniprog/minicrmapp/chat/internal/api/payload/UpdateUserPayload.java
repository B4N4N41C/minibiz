package ru.miniprog.minicrmapp.chat.internal.api.payload;

public record UpdateUserPayload(
        Long id,
        String username,
        String email,
        String password
) {
}
