package ru.miniprog.minicrmapp.chat.internal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.miniprog.minicrmapp.chat.internal.model.ChatRoom;
import ru.miniprog.minicrmapp.chat.internal.model.TypeChat;

@Component
public class ChatRoomMapper implements RowMapper<ChatRoom>{
    private final JdbcTemplate jdbcTemplate;
    private final MessageMapper messageMapper;
    
    public ChatRoomMapper(JdbcTemplate jdbcTemplate, MessageMapper messageMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.messageMapper = messageMapper;
    }

    @Override
    public ChatRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
         ChatRoom chatRoom = new ChatRoom();
            chatRoom.setId(rs.getLong("id"));
            chatRoom.setName(rs.getString("name"));
            chatRoom.setTypeChat(TypeChat.valueOf(rs.getString("type_chat")));
            chatRoom.setMessages(jdbcTemplate.query(
                "SELECT * FROM message WHERE chat_room_id = ?",
                messageMapper,
                chatRoom.getId()
            ));
            return chatRoom;
    }
}