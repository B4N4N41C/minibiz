package ru.miniprog.minicrmapp.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.kanban.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

}
