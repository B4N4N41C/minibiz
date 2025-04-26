package ru.miniprog.minicrmapp.chat.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.chat.internal.model.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByUsers_UsernameIgnoreCase(String username);
}