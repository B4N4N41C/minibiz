package ru.miniprog.minicrmapp.chat.internal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.miniprog.minicrmapp.chat.internal.model.Message;
import ru.miniprog.minicrmapp.chat.internal.model.MessageStatus;

@Component
public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setSenderName(rs.getString("sender_name"));
        message.setMessage(rs.getString("message"));
        message.setDate(rs.getTimestamp("date"));
        if(rs.getString("status") != null){
            try {
                message.setStatus(MessageStatus.valueOf(rs.getString("status")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        message.setChatRoom(rs.getLong("chat_room_id"));
        return message;
    }
}
