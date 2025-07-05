package ru.miniprog.minicrmapp.chat.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.chat.mapper.ChatRoomMapper;
import ru.miniprog.minicrmapp.chat.model.ChatRoom;
import ru.miniprog.minicrmapp.users.model.UserCrm;
import ru.miniprog.minicrmapp.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChatRoomRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository, ChatRoomMapper chatRoomMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.chatRoomMapper = chatRoomMapper;
    }


    public List<ChatRoom> findByUsers_UsernameIgnoreCase(String username) {
        String sql = """
            
                SELECT cr.* FROM chat_room cr
            JOIN chat_room_users cru ON cr.id = cru.chat_rooms_id
            JOIN user_crm u ON cru.users_id = u.id
            WHERE LOWER(u.username) = LOWER(?)
            """;
        List<ChatRoom> chatRooms = jdbcTemplate.query(sql, chatRoomMapper, username);
        chatRooms.forEach(this::loadUsers);
        return chatRooms;
    }

    public ChatRoom save(ChatRoom chatRoom) {
        if (chatRoom.getId() == null) {
            String sql = "INSERT INTO chat_room (name, type_chat) VALUES (?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    chatRoom.getName(),
                    chatRoom.getTypeChat().name());
            chatRoom.setId(id);
        } else {
            String sql = "UPDATE chat_room SET name = ?, type_chat = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    chatRoom.getName(),
                    chatRoom.getTypeChat().name(),
                    chatRoom.getId());
        }
        saveUsers(chatRoom);
        return chatRoom;
    }

    private void loadUsers(ChatRoom chatRoom) {
        String sql =
                """
                        SELECT u.*
                            FROM user_crm u
                        JOIN chat_room_users
                            cru ON u.id = cru.users_id
                                     WHERE cru.chat_rooms_id = ?
                        """;
        List<UserCrm> users = jdbcTemplate.query(sql, userRepository.getUserRowMapper(), chatRoom.getId());
        chatRoom.setUsers(users);
    }

    private void saveUsers(ChatRoom chatRoom) {
        String deleteSql = "DELETE FROM chat_room_users WHERE chat_rooms_id = ?";
        jdbcTemplate.update(deleteSql, chatRoom.getId());

        String insertSql = "INSERT INTO chat_room_users (chat_rooms_id, users_id) VALUES (?, ?)";
        for (UserCrm user : chatRoom.getUsers()) {
            jdbcTemplate.update(insertSql, chatRoom.getId(), user.getId());
        }
    }

    public Optional<ChatRoom> findById(Long id) {
        String sql = "SELECT * FROM chat_room WHERE id = ?";
        List<ChatRoom> chatRooms = jdbcTemplate.query(sql, chatRoomMapper, id);
        if (chatRooms.isEmpty()) {
            return Optional.empty();
        }
        ChatRoom chatRoom = chatRooms.get(0);
        loadUsers(chatRoom);
        return Optional.of(chatRoom);
    }
}