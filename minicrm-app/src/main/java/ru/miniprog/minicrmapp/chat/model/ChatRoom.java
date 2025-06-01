package ru.miniprog.minicrmapp.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.miniprog.minicrmapp.users.model.UserCrm;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "Модель чат-комнаты")
public class ChatRoom {
    @Schema(description = "Уникальный идентификатор чат-комнаты", example = "1")
    private Long id;

    @Schema(description = "Название чат-комнаты", example = "Общий чат")
    private String name;

    @Schema(description = "Тип чат-комнаты")
    private TypeChat typeChat;

    @JsonIgnore
    @Schema(description = "Список пользователей в чат-комнате")
    private List<UserCrm> users;

    @JsonManagedReference
    @Schema(description = "Список сообщений в чат-комнате")
    private List<Message> messages;
}
