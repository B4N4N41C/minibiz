package ru.miniprog.minicrmapp.chat.internal.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.miniprog.minicrmapp.chat.internal.api.payload.MessageDto;
import ru.miniprog.minicrmapp.chat.internal.api.payload.MessagePayload;
import ru.miniprog.minicrmapp.chat.internal.api.payload.NewChatRoomPayload;
import ru.miniprog.minicrmapp.chat.internal.model.*;
import ru.miniprog.minicrmapp.chat.internal.repository.ChatRoomRepository;
import ru.miniprog.minicrmapp.chat.internal.repository.MessageRepository;
import ru.miniprog.minicrmapp.chat.internal.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    ChatController(
            SimpMessagingTemplate simpMessagingTemplate,
            MessageRepository messageRepository,
            ChatRoomRepository chatRoomRepository,
            UserRepository userRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/private-message")
    public MessagePayload recMessage(@Payload MessagePayload message) {
        Long chatRoomId = message.chatRoom();
        messageRepository.save(new Message(
                null,
                message.senderName(),
                chatRoomId,
                message.message(),
                new Date(),
                MessageStatus.valueOf(message.status())));
        simpMessagingTemplate.convertAndSend("/chatroom/" + chatRoomId, message);
        System.out.println(message.toString());
        return message;
    }

    @GetMapping("/chat/messages")
    public List<MessageDto> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(msg -> new MessageDto(
                        msg.getId(),
                        msg.getSenderName(),
                        msg.getChatRoom(),
                        msg.getMessage(),
                        msg.getDate(),
                        msg.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/getAllChatRoom/{username}")
    public List<ChatRoom> getAllChatRoomForUser(@PathVariable String username) {
        return chatRoomRepository.findByUsers_UsernameIgnoreCase(username);
    }

    @PostMapping("/chat/creat")
    public ChatRoom postMethodName(@RequestBody NewChatRoomPayload newChatRoomPayload) {
        List<UserCrm> users = new ArrayList<>();
        for (Long userCrmDtoId : newChatRoomPayload.users()) {
            users.add(userRepository.findById(userCrmDtoId).get());
        }
        return chatRoomRepository.save(new ChatRoom(null, newChatRoomPayload.name(),
                TypeChat.valueOf(newChatRoomPayload.typeChat()), users, null));
    }
}