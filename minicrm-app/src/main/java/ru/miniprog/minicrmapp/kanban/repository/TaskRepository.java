package ru.miniprog.minicrmapp.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrmapp.kanban.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
