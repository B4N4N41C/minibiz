package ru.miniprog.minicrmapp.chat.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.chat.internal.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
