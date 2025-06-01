package ru.miniprog.minicrmapp.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCrmDTO {
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "ivanov")
    private String username;
    @Schema(description = "Email пользователя", example = "ivanov@example.com")
    private String email;
}


