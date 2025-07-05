package ru.miniprog.minicrmapp.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.miniprog.minicrmapp.users.model.UserCrm;

import java.util.List;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeChat getTypeChat() {
        return typeChat;
    }

    public void setTypeChat(TypeChat typeChat) {
        this.typeChat = typeChat;
    }

    public List<UserCrm> getUsers() {
        return users;
    }

    public void setUsers(List<UserCrm> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ChatRoom(Long id, String name, TypeChat typeChat, List<UserCrm> users, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.typeChat = typeChat;
        this.users = users;
        this.messages = messages;
    }

    public ChatRoom() {
    }

    @Override
    public String toString() {
        return "ChatRoom [id=" + id + ", name=" + name + ", typeChat=" + typeChat + ", users=" + users + ", messages="
                + messages + "]";
    }

}
