package ru.miniprog.minicrmapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.miniprog.minicrmapp.chat.api.ChatController;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatController chatController;

    @Test
    void recMessage_ShouldSaveAndBroadcastMessage() {
        // Arrange
        MessagePayload payload = new MessagePayload(
                "sender", 1L, "Hello", "MESSAGE");

        Message expectedMessage = new Message(
                null, "sender", 1L, "Hello", new Date(), MessageStatus.MESSAGE);

        when(messageRepository.save(any(Message.class))).thenReturn(expectedMessage);

        // Act
        MessagePayload result = chatController.recMessage(payload);

        // Assert
        verify(messageRepository).save(any(Message.class));
        verify(simpMessagingTemplate).convertAndSend("/chatroom/1", payload);
        assertEquals(payload, result);
    }

    @Test
    void getAllMessages_ShouldReturnAllMessages() {
        // Arrange
        List<Message> messages = Arrays.asList(
                new Message(1L, "user1", 1L, "text1", new Date(), MessageStatus.MESSAGE),
                new Message(2L, "user2", 1L, "text2", new Date(), MessageStatus.LEAVE)
        );

        when(messageRepository.findAll()).thenReturn(messages);

        // Act
        var result = chatController.getAllMessages();

        // Assert
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).senderName());
        assertEquals("text2", result.get(1).message());
        assertEquals(MessageStatus.LEAVE.name(), result.get(1).status());
    }

    @Test
    void getAllChatRoomForUser_ShouldReturnUserChatRooms() {
        // Arrange
        String username = "testUser";
        UserCrm user = new UserCrm(1L, username, "pass", "email", null, null);

        List<ChatRoom> expectedRooms = Arrays.asList(
                new ChatRoom(1L, "Room1", TypeChat.USER_CHAT, List.of(user), null),
                new ChatRoom(2L, "Room2", TypeChat.GROUP_CHAT, List.of(user), null)
        );

        when(chatRoomRepository.findByUsers_UsernameIgnoreCase(username))
                .thenReturn(expectedRooms);

        // Act
        var result = chatController.getAllChatRoomForUser(username);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Room1", result.get(0).getName());
        assertEquals(TypeChat.GROUP_CHAT, result.get(1).getTypeChat());
        assertEquals(username, result.get(0).getUsers().get(0).getUsername());
    }

    @Test
    void createChatRoom_ShouldSaveNewChatRoom() {
        // Arrange
        NewChatRoomPayload payload = new NewChatRoomPayload(
                "New Room", "GROUP_CHAT", List.of(1L, 2L));

        UserCrm user1 = new UserCrm(1L, "user1", "pass1", "email1", null, null);
        UserCrm user2 = new UserCrm(2L, "user2", "pass2", "email2", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        ChatRoom savedRoom = new ChatRoom(
                1L, "New Room", TypeChat.GROUP_CHAT, List.of(user1, user2), null);

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(savedRoom);

        // Act
        var result = chatController.postMethodName(payload);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2, result.getUsers().size());
        assertEquals(TypeChat.GROUP_CHAT, result.getTypeChat());

        verify(userRepository, times(2)).findById(anyLong());
        verify(chatRoomRepository).save(any(ChatRoom.class));
    }

    @Test
    void createChatRoom_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        NewChatRoomPayload payload = new NewChatRoomPayload(
                "New Room", "TASK_CHAT", List.of(99L));

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            chatController.postMethodName(payload);
        });
    }
}

