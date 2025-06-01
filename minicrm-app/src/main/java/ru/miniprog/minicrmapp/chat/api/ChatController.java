package ru.miniprog.minicrmapp.chat.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.modulith.ApplicationModule;
import org.springframework.web.bind.annotation.*;
import ru.miniprog.minicrmapp.chat.api.payload.MessageDto;
import ru.miniprog.minicrmapp.chat.api.payload.MessagePayload;
import ru.miniprog.minicrmapp.chat.api.payload.NewChatRoomPayload;
import ru.miniprog.minicrmapp.chat.model.ChatRoom;
import ru.miniprog.minicrmapp.chat.model.Message;
import ru.miniprog.minicrmapp.chat.model.MessageStatus;
import ru.miniprog.minicrmapp.chat.model.TypeChat;
import ru.miniprog.minicrmapp.chat.repository.ChatRoomRepository;
import ru.miniprog.minicrmapp.chat.repository.MessageRepository;
import ru.miniprog.minicrmapp.users.model.UserCrm;
import ru.miniprog.minicrmapp.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ApplicationModule(allowedDependencies = "ru.miniprog.minicrmapp.users")
@Tag(name = "Управление чатами", description = "API для управления чатами и сообщениями")
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

    @Operation(summary = "Отправить приватное сообщение", description = "Отправляет приватное сообщение в чат-комнату")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сообщение успешно отправлено"),
        @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
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

    @Operation(summary = "Получить все сообщения", description = "Возвращает список всех сообщений в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список сообщений успешно получен"),
        @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @GetMapping("/chat/messages")
    public List<MessageDto> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(msg -> new MessageDto(
                        msg.getId(),
                        msg.getSenderName(),
                        msg.getChatRoom(),
                        msg.getMessage(),
                        msg.getDate(),
                        msg.getStatus().name()))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получить чат-комнаты пользователя", description = "Возвращает список всех чат-комнат для указанного пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список чат-комнат успешно получен"),
        @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/getAllChatRoom/{username}")
    public List<ChatRoom> getAllChatRoomForUser(@PathVariable String username) {
        return chatRoomRepository.findByUsers_UsernameIgnoreCase(username);
    }

    @Operation(summary = "Создать новую чат-комнату", description = "Создает новую чат-комнату с указанными пользователями")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат-комната успешно создана"),
        @ApiResponse(responseCode = "400", description = "Неверные данные чат-комнаты"),
        @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
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