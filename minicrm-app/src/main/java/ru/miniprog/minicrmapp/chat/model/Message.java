package ru.miniprog.minicrmapp.chat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "Модель сообщения в чате")
public class Message {
    @Schema(description = "Уникальный идентификатор сообщения", example = "1")
    private Long id;

    @Schema(description = "Имя отправителя", example = "ivanov")
    private String senderName;

    @JsonBackReference
    @Schema(description = "ID чат-комнаты", example = "1")
    private Long chatRoom;

    @Schema(description = "Текст сообщения", example = "Привет!")
    private String message;

    @Schema(description = "Дата и время отправки сообщения")
    private Date date = new Date();

    @Schema(description = "Статус сообщения")
    private MessageStatus status;
}