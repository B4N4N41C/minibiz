package ru.miniprog.minicrmapp.chat.internal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatRoom {
    private Long id;
    private String name;
    private TypeChat typeChat;
    @JsonIgnore
    private List<UserCrm> users;
    @JsonManagedReference
    private List<Message> messages;
}
