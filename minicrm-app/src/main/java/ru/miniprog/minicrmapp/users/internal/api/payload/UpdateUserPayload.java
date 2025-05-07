package ru.miniprog.minicrmapp.users.internal.api.payload;

public record UpdateUserPayload(
        Long id,
        String username,
        String email,
        String password
) {
}
