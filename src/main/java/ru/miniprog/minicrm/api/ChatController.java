package ru.miniprog.minicrm.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.miniprog.minicrm.api.payload.MessagePayload;
import ru.miniprog.minicrm.model.ChatRoom;
import ru.miniprog.minicrm.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.miniprog.minicrm.model.MessageStatus;
import ru.miniprog.minicrm.repository.ChatRoomRepository;
import ru.miniprog.minicrm.repository.MessageRepository;

import java.util.Date;
import java.util.List;

@RestController
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    ChatController(
            SimpMessagingTemplate simpMessagingTemplate,
            MessageRepository messageRepository,
            ChatRoomRepository chatRoomRepository
    ) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @MessageMapping("/private-message")
    public MessagePayload recMessage(@Payload MessagePayload message) {
        Long chatRoomId = message.chatRoom();
        messageRepository.save(new Message(
                null,
                message.senderName(),
                chatRoomRepository.getReferenceById(chatRoomId),
                message.message(),
                new Date(),
                MessageStatus.valueOf(message.status())));
        simpMessagingTemplate.convertAndSend("/chatroom/" + chatRoomId, message);
        System.out.println(message.toString());
        return message;
    }

    @GetMapping("/getAllChatRoom/{username}")
    public List<ChatRoom> getAllChatRoomForUser(@PathVariable String username) {
        return chatRoomRepository.findByUsers_UsernameIgnoreCase(username);
    }
    
}