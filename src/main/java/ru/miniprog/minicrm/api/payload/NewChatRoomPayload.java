package ru.miniprog.minicrm.api.payload;

import ru.miniprog.minicrm.dto.UserCrmDTO;

import java.util.List;

public record NewChatRoomPayload(
	String name,
	String typeChat,
	List<Long> users
) {

}
