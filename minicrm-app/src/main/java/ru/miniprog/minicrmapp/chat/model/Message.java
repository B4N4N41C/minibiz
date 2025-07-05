package ru.miniprog.minicrmapp.chat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Long getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(Long chatRoom) {
		this.chatRoom = chatRoom;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public Message(Long id, String senderName, Long chatRoom, String message, Date date, MessageStatus status) {
		this.id = id;
		this.senderName = senderName;
		this.chatRoom = chatRoom;
		this.message = message;
		this.date = date;
		this.status = status;
	}

	public Message() {
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", senderName=" + senderName + ", chatRoom=" + chatRoom + ", message=" + message
				+ ", date=" + date + ", status=" + status + "]";
	}
}