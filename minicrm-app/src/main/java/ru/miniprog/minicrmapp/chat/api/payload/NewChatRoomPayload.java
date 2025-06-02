package ru.miniprog.minicrmapp.chat.api.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO для создания новой чат-комнаты")
public record NewChatRoomPayload(
	@Schema(description = "Название чат-комнаты", example = "Общий чат")
	@NotBlank(message = "Название чата не может быть пустым")
	@Size(min = 3, max = 50, message = "Название чата должно быть от 3 до 50 символов")
	String name,

	@Schema(description = "Тип чат-комнаты", example = "GROUP_CHAT")
	@NotBlank(message = "Тип чата не может быть пустым")
	String typeChat,

	@Schema(description = "Список ID пользователей", example = "[1, 2, 3]")
	@NotEmpty(message = "Список пользователей не может быть пустым")
	List<Long> users
) {

}
