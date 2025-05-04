package ru.miniprog.minicrmapp.chat.internal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private Long id;
    private String senderName;
    @JsonBackReference
    private Long chatRoom;
    private String message;
    private Date date = new Date();
    private MessageStatus status;
}