package ru.miniprog.minicrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.miniprog.minicrm.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Дополнительные методы, если необходимо
}