package ru.miniprog.minicrmapp.chat.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.chat.internal.model.UserCrm;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserCrm, Long> {
    Optional<UserCrm> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}