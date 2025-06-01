package ru.miniprog.minicrmapp.users.api.payload;

public record UpdateUserPayload(
        Long id,
        String username,
        String email,
        String password
) {
}
