package ru.miniprog.minicrm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderName;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    private String message;
    private Date date = new Date();
    private MessageStatus status;
}