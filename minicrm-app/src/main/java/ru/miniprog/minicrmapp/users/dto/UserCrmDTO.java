package ru.miniprog.minicrmapp.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserCrmDTO {
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "ivanov")
    private String username;
    @Schema(description = "Email пользователя", example = "ivanov@example.com")
    private String email;

    public UserCrmDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserCrmDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


