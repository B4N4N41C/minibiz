package ru.miniprog.minicrmapp.chat.internal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.miniprog.minicrmapp.chat.internal.mapper.MessageMapper;
import ru.miniprog.minicrmapp.chat.internal.model.Message;

import java.util.List;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MessageMapper messageMapper;

    public MessageRepository(JdbcTemplate jdbcTemplate, MessageMapper messageMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.messageMapper = messageMapper;
    }

    public Message save(Message message) {
        if (message.getId() == null) {
            String sql = "INSERT INTO message (sender_name, chat_room_id, message, date, status) VALUES (?, ?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    message.getSenderName(),
                    message.getChatRoom(),
                    message.getMessage(),
                    message.getDate(),
                    message.getStatus().name());
                    message.setId(id);
        } else {
            String sql = "UPDATE message SET sender_name = ?, chat_room_id = ?, message = ?, date = ?, status = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    message.getSenderName(),
                    message.getChatRoom(),
                    message.getMessage(),
                    message.getDate(),
                    message.getStatus().name(),
                    message.getId());
        }
        return message;
    }

    public List<Message> findAllForChatRoom(Long chatRoomId) {
        String sql = "SELECT * FROM message WHERE chat_room_id = ?";
        List<Message> messages = jdbcTemplate.query(sql, messageMapper, chatRoomId);
        return messages;
    }

    public List<Message> findAll() {
        String sql = "SELECT * FROM message";
        List<Message> messages = jdbcTemplate.query(sql, messageMapper);
        return messages;
    }
}
