package ru.miniprog.minicrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrm.model.UserCrm;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserCrm, Long> {
    Optional<UserCrm> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}