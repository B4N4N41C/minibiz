package ru.miniprog.minicrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TypeChat typeChat;
    @ManyToMany
    @JsonIgnore
    private List<UserCrm> users;
    @OneToMany
    @JoinColumn(name = "chat_room_id")
    @JsonManagedReference
    private List<Message> messages;
}
