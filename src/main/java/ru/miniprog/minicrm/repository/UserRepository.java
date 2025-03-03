package ru.miniprog.minicrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrm.model.UserCrm;

@Repository
public interface UserRepository extends JpaRepository<UserCrm, Integer> {
    // Дополнительные методы, если необходимо
}