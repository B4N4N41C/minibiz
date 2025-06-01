package ru.miniprog.minicrmapp.chat.api.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для создания новой чат-комнаты")
public record NewChatRoomPayload(
	@Schema(description = "Название чат-комнаты", example = "Общий чат")
	String name,

	@Schema(description = "Тип чат-комнаты", example = "GROUP_CHAT")
	String typeChat,

	@Schema(description = "Список ID пользователей", example = "[1, 2, 3]")
	List<Long> users
) {

}
