package ru.miniprog.minicrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.miniprog.minicrm.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
