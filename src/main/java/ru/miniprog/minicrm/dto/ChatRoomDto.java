package ru.miniprog.minicrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import ru.miniprog.minicrm.model.TypeChat;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link ru.miniprog.minicrm.model.ChatRoom}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoomDto implements Serializable {
    String name;
    TypeChat typeChat;
    List<MessageDto> messages;
}