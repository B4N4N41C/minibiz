package ru.miniprog.minicrmapp.chat.internal.api.payload;

import java.util.List;

public record NewChatRoomPayload(
	String name,
	String typeChat,
	List<Long> users
) {

}
